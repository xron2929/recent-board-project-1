package com.example.demo.board;

import com.example.demo.cookie.CookieManager;
import com.example.demo.entityjoin.NoneUserBoardSaveDataDto;
import com.example.demo.role.RoleStatus;
import com.example.demo.security.authentication.AuthenticationManager;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.TokenStatus;
import com.example.demo.security.jwt.UserRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class BoardWriteController {
    @Autowired
    JwtManager jwtManager;
    @Autowired
    EditBoardMapper editBoardMapper;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    BoardService boardService;

    @Autowired
    CookieManager cookieManager;
    @GetMapping("/write")
    @ApiOperation("board-write 뷰 반환")
    public String write(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        Map<String,String> magnageUrlMap = new HashMap<>();
        magnageUrlMap.put(RoleStatus.ROLE_ANONYMOUS.name(),"board/write/none-user-write");
        magnageUrlMap.put(RoleStatus.ROLE_SITE_USER.name(),"board/write/user-write");
        magnageUrlMap.put(RoleStatus.ROLE_OAUTH_USER.name(),"board/write/user-write");
        magnageUrlMap.put(RoleStatus.ROLE_ADMIN.name(),"board/write/user-write");

        System.out.println("readController - authenticationManager = " + authenticationManager);
        String refreshToken = jwtManager.getRefreshToken(request);
        TokenStatus isSafeJwt = jwtManager.validation(refreshToken);
        if(isSafeJwt == TokenStatus.NONE) {
            return magnageUrlMap.get(RoleStatus.ROLE_ANONYMOUS.name());
        }
        authenticationManager.checkAuthenticationManager(request, response);
        // accessToken,refresh 토큰 null 일 떄 처리 로직
        // null이 아니면 아래서 검출
        String authenticationView = authenticationManager.getAuthenticationView(request, response, magnageUrlMap);
        return authenticationView;
    }
    @PostMapping("/board/none-user")
    @ApiOperation("board 업데이트 추가(NONE_USER)")
    @ResponseBody
    public String writeNoneUser(HttpServletRequest request,HttpServletResponse response,
                                @RequestBody BoardEditNoneUserDto boardEditUserDto) throws JsonProcessingException {

        String accessToken = authenticationManager.checkAuthenticationManager(request, response);
        System.out.println("accessToken = " + accessToken);
        if(accessToken == "null") {
            System.out.println("성공");

            System.out.println("boardEditUserDto.getId() = " + boardEditUserDto.getId());
            System.out.println("boardEditUserDto.getContent() = " + boardEditUserDto.getContent());
            System.out.println("boardEditUserDto.getTitle() = " + boardEditUserDto.getTitle());
            System.out.println("writeUser - userName = " + boardEditUserDto.getUsername());

            String uuidCookie = cookieManager.getUUidCookie(request);
            NoneUserBoardSaveDataDto userBoardSaveDataDto = NoneUserBoardSaveDataDto.builder()
                    .id(boardEditUserDto.getId())
                    .userId(uuidCookie)
                    .title(boardEditUserDto.getTitle())
                    .content(boardEditUserDto.getContent())
                    .nickname(boardEditUserDto.getUsername())
                    .password(boardEditUserDto.getPassword())
                    .isSecret(boardEditUserDto.isSecret()).build();

            System.out.println("BoardWriteController - writeNoneUser() uuidCookie = "+uuidCookie);
            editBoardMapper.insertNoneUserBoard(request,userBoardSaveDataDto);
            return "ok";
        }
        System.out.println("실패");
        return "fail";
    }

    @PostMapping("/board/user")
    @ApiOperation("board 업데이트 추가(USER)")
    @ResponseBody
    public String writeUser(HttpServletRequest request,HttpServletResponse response,
                            @RequestBody BoardEditUserDto boardEditUserDto) throws JsonProcessingException {

        String accessToken = authenticationManager.checkAuthenticationManager(request, response);
        UserRequestDto userRequestDto = jwtManager.getUserRequestDto(accessToken);
        if(accessToken != "null") {
            System.out.println("성공");
            System.out.println("boardEditUserDto.getContent() = " + boardEditUserDto.getContent());
            System.out.println("boardEditUserDto.getTitle() = " + boardEditUserDto.getTitle());
            editBoardMapper.insertUserBoard(boardEditUserDto,userRequestDto);
            System.out.println("writeUser - userName = " + boardEditUserDto.getUsername());
            return "ok";
        }
        System.out.println("실패");
        return "fail";
    }


    @PostMapping("/hello5")
    @ApiOperation("homeUrl로 리다이렉트")
    public String writeEnd(@RequestBody String board, ModelMap modelMap) {

        System.out.println("board = " + board);
        return "redirect:/";
    }
}
