package com.example.demo.board;


import com.example.demo.role.RoleStatus;
import com.example.demo.entityjoin.BoardResponseDslDto;
import com.example.demo.entityjoin.BoardResponseDto;
import com.example.demo.feedback.FeedBackCount;
import com.example.demo.feedback.FeedBackCountService;
import com.example.demo.feedback.JoinStatus;
import com.example.demo.security.authentication.AuthenticationManager;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.TokenStatus;
import com.example.demo.security.jwt.UserRequestDto;
import com.example.demo.user.UserIdAndPasswordDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class BoardReadController {
    @Autowired
    BoardService boardService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    JwtManager jwtManager;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    FeedBackCountService feedBackCountApi;
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
        TokenStatus isSafeJwt = jwtManager.validaition(refreshToken);
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
        String userAuthority = boardService.getUserAuthority(boardId);
        System.out.println(" ReadController - userAuthority = " + userAuthority);
        System.out.println("ReadController - userAuthority.equals(RoleStatus.ROLE_OAUTH_USER) = " + userAuthority.equals(RoleStatus.ROLE_OAUTH_USER.name()));
        System.out.println("ReadController - userAuthority == RoleStatus.ROLE_OAUTH_USER = " + userAuthority == RoleStatus.ROLE_OAUTH_USER.name());
        String accessToken = jwtManager.getAccessToken(request);
        if(accessToken == null && userAuthority.equals(RoleStatus.ROLE_ANONYMOUS.name())) {
            System.out.println("ReadController -  유저 아님 " );
            return "ok";
        }
        String authorityName = jwtManager.getAuthorityName(accessToken);
        if(authorityName == null && userAuthority.equals(RoleStatus.ROLE_ANONYMOUS.name())) {
            System.out.println("ReadController -  유저 아님 " );
            return "ok";
        }
        if(authorityName.equals(RoleStatus.ROLE_ADMIN.name())) {
            System.out.println("ReadController -  운영자 " );
            return "ok";
        }
        System.out.println("READCONTROLLER-BUG1");
        if(userAuthority.equals(RoleStatus.ROLE_OAUTH_USER.name())) {
            UserIdAndPasswordDto userIdAndPassword = boardService.findUserIdAndPassword(boardId);
            UserRequestDto userRequestDto = jwtManager.getUserRequestDto(accessToken);
            System.out.println("READCONTROLLER-BUG2");
            System.out.println("ReadController - userId = " + userRequestDto.getUserId());
            System.out.println("ReadController - board.getUserId() = " + userIdAndPassword.getUserId());
            System.out.println("userIdAndPassword.getPassword() = " + userIdAndPassword.getPassword());
            System.out.println("userRequestDto.getPassword() = " + userRequestDto.getPassword());
            System.out.println("userRequestDto.getUserId().equals(userIdAndPassword.getUserId()) = " + userRequestDto.getUserId().equals(userIdAndPassword.getUserId()));
            if(isEqaulUserNameAndPassword(userIdAndPassword,userRequestDto)) {
                System.out.println("ReadController -  같은 사용자 " );
                return "ok";
            }
        }
        return "다른 사용자";
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

