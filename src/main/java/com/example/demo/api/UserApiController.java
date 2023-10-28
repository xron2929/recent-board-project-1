package com.example.demo.api;


import com.example.demo.authority.Authority;
import com.example.demo.board.Board;
import com.example.demo.board.BoardService;
import com.example.demo.comment.CommentService;
import com.example.demo.cookie.CookieManager;
import com.example.demo.entityjoin.UserAndBoardDto;
import com.example.demo.home.HomeController;
import com.example.demo.role.RoleStatus;
import com.example.demo.security.authentication.AuthenticationManager;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.TokenStatus;
import com.example.demo.security.jwt.UserRequestDto;
import com.example.demo.user.UserService;
import com.example.demo.user.defaultuser.DefaultMember;
import com.example.demo.user.oauthuser.OauthUserFirstJoinDto;
import com.example.demo.userAuthority.UserAuthority;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
public class UserApiController {
    // 나중에 서비스로직으로 바꾸기..
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    JwtManager jwtManager;
    @Autowired
    CookieManager cookieManager;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    BoardService boardService;

    @GetMapping("/user-noneuser/account")
    @ApiOperation(value = "board를 가져올 때 user랑 NoneUser에 맞게 각각 처리")
    public UserIdAndNickname getUserAndNoneUserBoard(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String isExistence = authenticationManager.checkAuthenticationManager(request, response);
        if(isExistence == "null") {
            System.out.println("isExistence = " + isExistence);
            return UserIdAndNickname.builder().userId(cookieManager.getUUidCookie(request))
                    .nickname(cookieManager.getUUidCookie(request)).build();
        }
        String accessToken = jwtManager.getAccessToken(request);
        UserRequestDto userRequestDto = jwtManager.getUserRequestDto(accessToken);
        System.out.println("userApiController - getUserAndNoneUserBoard() - userRequestDto = " + userRequestDto);
        return  UserIdAndNickname.builder()
                .userId(userRequestDto.getUserId())
                .nickname(userRequestDto.getNickname()).build();

    }

    @GetMapping("/user/account")
    @ApiOperation(value = "해당 사용자가 유저일 때만 데이터 가져옴")
    public UserIdAndNickname getUserDto(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        String isExistence = authenticationManager.checkAuthenticationManager(request, response);
        if(isExistence == "null") {
            return null;
        }
        String accessToken = jwtManager.getAccessToken(request);

        log.info("accessToken = {}",accessToken);
        System.out.println("//dsfsdf");
        System.out.println("accessToken = " + accessToken);
        UserRequestDto userRequestDto = jwtManager.getUserRequestDto(accessToken);
        return UserIdAndNickname.builder()
                .userId(userRequestDto.getUserId())
                .nickname(userRequestDto.getNickname()).build();
    }

    @GetMapping("/none/user/account")
    @ApiOperation(value = "해당 사용자가 일반 유저가 아닐 경우에만 데이터 가져옴")
    public String getNoneUserDto(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        return cookieManager.getUUidCookie(request);
    }
    // 만약 업데이트 안되는 거면 그거 까먹지 마리
    @GetMapping("/user/logout")
    @ApiOperation(value = "해당 사용자가 일반 유저가 아닐 경우에만 데이터 가져옴")
    public void logOut(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        cookieManager.makeZeroSecondCookie("accessToken",response);
        cookieManager.makeZeroSecondCookie("refreshToken",response);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    static class ResponseAuthenticationDto {
        private String role;
    }

    @GetMapping("/role")
    @ApiOperation("권한 정보를 반환")
    @ResponseBody
    public ResponseAuthenticationDto findAuthentication(HttpServletRequest request) throws JsonProcessingException {
        ResponseAuthenticationDto responseAuthenticationDto = new ResponseAuthenticationDto();
        String accessToken = jwtManager.getAccessToken(request);
        System.out.println("UserApiController - accessToken = " + accessToken);
        if(accessToken == null) {
            responseAuthenticationDto.setRole("비회원");
            return responseAuthenticationDto;
        }
        UserRequestDto userRequestDto = jwtManager.getUserRequestDto(accessToken);
        List<UserAuthority> userAuthorities = userRequestDto.getUserAuthorities();



        for (UserAuthority userAuthority:userAuthorities) {
            if(userAuthority.getAuthority().getAuthorityName().matches(RoleStatus.ROLE_ADMIN.name())) {
                responseAuthenticationDto.setRole("어드민");
                return responseAuthenticationDto;
            }
            if(userAuthority.getAuthority().getAuthorityName().matches(RoleStatus.ROLE_SITE_USER.name())) {
                responseAuthenticationDto.setRole("일반 회원가입 유저");
                return responseAuthenticationDto;
            }
            if(userAuthority.getAuthority().getAuthorityName().matches(RoleStatus.ROLE_OAUTH_USER.name())) {
                responseAuthenticationDto.setRole("OAUTH 유저");
                return responseAuthenticationDto;
            }
            if(userAuthority.getAuthority().getAuthorityName().matches(RoleStatus.ROLE_ANONYMOUS.name())) {
                responseAuthenticationDto.setRole("비회원");
                return responseAuthenticationDto;
            }
        }
        responseAuthenticationDto.setRole("권한 에러");
        return responseAuthenticationDto;
    }


}

