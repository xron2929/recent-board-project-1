package com.example.demo.board;


import com.example.demo.boradAndUser.MemberBoardQueryDTO;
import com.example.demo.role.RoleStatus;
import com.example.demo.boradAndUser.BoardResponseDto;
import com.example.demo.feedback.FeedBackCount;
import com.example.demo.feedback.FeedBackCountService;
import com.example.demo.feedback.JoinStatus;
import com.example.demo.security.authentication.AuthenticationManager;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.TokenStatus;
import com.example.demo.security.jwt.UserRequestDto;
import com.example.demo.user.IsAllowedAuthorityStatus;
import com.example.demo.user.UserIdAndIsAllowedAuthorityStatus;
import com.example.demo.user.UserIdAndPasswordDto;
import com.example.demo.user.UserService;
import com.example.demo.util.BoardCalculator;
import com.example.demo.util.BoardQueryDto;
import com.example.demo.util.UserManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BoardReadController {
    @Autowired
    UserService userService;
    @Autowired
    BoardService boardService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    JwtManager jwtManager;
    @Autowired
    UserManager userManager;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    FeedBackCountService feedBackCountApi;
    @Value("${server.domain.url}")
    private String serverDomainUrl;
    @GetMapping("/boards/{boardId}")
    @ApiOperation("board 뷰 반환")
    // @ResponseBody
    public String realRead(HttpServletRequest request, HttpServletResponse response, @PathVariable String boardId) throws JsonProcessingException {
        Map<String,String> magnageUrlMap = new HashMap<>();
        magnageUrlMap.put(RoleStatus.ROLE_ANONYMOUS.name(),"board/read/none-user-read");
        magnageUrlMap.put(RoleStatus.ROLE_SITE_USER.name(),"board/read/user-read");
        magnageUrlMap.put(RoleStatus.ROLE_OAUTH_USER.name(),"board/read/user-read");
        magnageUrlMap.put(RoleStatus.ROLE_ADMIN.name(),"board/read/admin-read");

        System.out.println("readController - authenticationManager = " + authenticationManager);
        String refreshToken = jwtManager.getRefreshToken(request);
        TokenStatus isSafeJwt = jwtManager.validation(refreshToken);
        if(isSafeJwt == TokenStatus.NONE) {
            return magnageUrlMap.get(RoleStatus.ROLE_ANONYMOUS.name());
        }

        // accessToken,refresh 토큰 null 일 떄authenticationManager.checkAuthenticationManager(request, response); 처리 로직
        // null이 아니면 아래서 검출
        String authenticationView = authenticationManager.getAuthenticationView(request, response, magnageUrlMap);
        System.out.println("realRead: authenticationView = " + authenticationView);
        return authenticationView;
    }
    @GetMapping("/board/finalId")
    @ApiOperation("board 마지막 id 읽기")
    @ResponseBody
    public Long finalBoardId() {
        return boardService.getFinalBoardId();
    }
    @ApiOperation("board 데이터 읽기")
    @GetMapping("/boards/{boardId}/data")
    @ResponseBody
    public BoardResponseDslDto realRead2(@PathVariable Long boardId) throws Exception {
        System.out.println("boardNumber = " + boardId);
        BoardResponseDto boardResponseDto = boardService.getBoardResponseDto(boardId);
        System.out.println("boardResponseDto = " + boardResponseDto);
        FeedBackCount feedBackCount = feedBackCountApi.getFeedBackCount(JoinStatus.BOARD, boardResponseDto.getId());
        BoardResponseDslDto boardResponseDslDto = BoardResponseDslDto.builder()
                .id(boardResponseDto.getId())
                .title(boardResponseDto.getTitle())
                .contents(boardResponseDto.getContents())
                .boardWriterName(boardResponseDto.getBoardWriterName())
                .createdDate(boardResponseDto.getCreatedDate())
                .lastModifiedDate(boardResponseDto.getLastModifiedDate())
                .likeCount(feedBackCount.getLikeCount())
                .disLikeCount(feedBackCount.getDisLikeCount())
                .build();
        return boardResponseDslDto;
    }

    @GetMapping("/board/userAuthority")
    @ApiOperation("board 조회 때 해당 board의 권한 조회(websocket의 권한때문에 조회함) ")
    @ResponseBody
    public String getUserAuthority(@RequestParam Long boardId, HttpServletRequest request) throws JsonProcessingException {
        UserAuthorityAndUserIdDto userAuthorityAndUserIdDto = boardService.getAuthorityAndUserId(boardId);

        UserIdAndIsAllowedAuthorityStatus userIdAndIsAllowedAuthorityStatus = userManager.checkUserIdAndValidation(userAuthorityAndUserIdDto,request);
        if(userIdAndIsAllowedAuthorityStatus.getIsAllowedAuthorityStatus() == IsAllowedAuthorityStatus.SAME_USER_ACCOUNT) {
            return "ok";
        }
        if(userIdAndIsAllowedAuthorityStatus.getIsAllowedAuthorityStatus() == IsAllowedAuthorityStatus.UN_SAME_USER_ACCOUNT) {
            return "다른 사용자";
        }
        if(userIdAndIsAllowedAuthorityStatus.getIsAllowedAuthorityStatus() == IsAllowedAuthorityStatus.HIGHER_READER_ACCOUNT) {
            return "ok";
        }
        return "에러";
    }


    @GetMapping
    @ApiOperation("루트 뷰(게시판 목록들이 포함된)를 반환")
    public String root(@RequestParam(defaultValue = "1") Long pageQuantity, @RequestParam(defaultValue = "20") Long boardQuantity, Model model) {
        model.addAttribute("serverDomainUrl", serverDomainUrl);


        model.addAttribute("jsDomainUrl", serverDomainUrl+"/js/boards/un-search/un-search-script.js");
        model.addAttribute("searchDomainUrl", serverDomainUrl+"/board/search");
        model.addAttribute("naverDomainUrl", serverDomainUrl+"/oauth2/authorization/naver");

        return "boards/un-search/un-search";
    }

    @GetMapping("/boards")
    @ApiOperation("pageQuantity, boardQuantity 바탕으로 boards 데이터 목록을 반환")
    @ResponseBody
    public List<MemberBoardQueryDTO> getBoardQueryDtos(@RequestParam(defaultValue="1") int pageQuantity, @RequestParam(defaultValue="20") int boardQuantity) {


        BoardCalculator boardCalculator = new BoardCalculator();
        BoardQueryDto boardQueryDto = boardCalculator.calculate(pageQuantity,boardQuantity);
        System.out.println("boardQueryDto = " + boardQueryDto);
        List<MemberBoardQueryDTO> boards = userService.findBoards(boardQueryDto.getPrevBoardQuantity(),boardQueryDto.getBoardQuantity());
        return boards;
    }

    private boolean isEqaulUserNameAndPassword(UserIdAndPasswordDto userIdAndPasswordDto, UserRequestDto userRequestDto) {
        if(!bCryptPasswordEncoder.matches("겟인데요",userRequestDto.getPassword())) {
            System.out.println("error1");
            return false;
        }
        if(!userRequestDto.getUserId().equals(userIdAndPasswordDto.getUserId())) {
            System.out.println("error2");
            return false;
        }
        return true;
    }
}

