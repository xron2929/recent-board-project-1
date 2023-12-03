package com.example.demo.user;

import com.example.demo.role.RoleStatus;
import com.example.demo.user.api.UserIdAndNickname;
import com.example.demo.user.authority.Authority;
import com.example.demo.security.authentication.AuthenticationManager;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.UserRequestDto;
import com.example.demo.user.defaultuser.DefaultMember;
import com.example.demo.user.userAuthority.UserAuthority;
import com.example.demo.user.userAuthority.UserAuthorityService;
import com.example.demo.util.ResponseAuthenticationDto;
import com.example.demo.util.UserManager;
import com.example.demo.util.cookie.CookieManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.Keys;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class UserController {
    @Autowired
    private UserService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserAuthorityService userAuthorityService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    CookieManager cookieManager;
    @Autowired
    UserManager userManager;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtManager jwtManager;
    @Autowired UserService userService;
    @Value("${jwt.secretkey}")
    String secretKey;
    Key key;
    @PostConstruct
    private void makeKey() {
        secretKey = "c2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQtc2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQK";
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    @GetMapping("/principal")
    @ApiOperation("어드민을 포함한 몸든 권한을 보여줌")
    public String user(HttpServletRequest request, HttpServletResponse response, Model model) throws JsonProcessingException {
        log.info("user");
        String accessToken = authenticationManager.checkAuthenticationManager(request, response);
        // 여기서 accessToken이 문제임
        UserRequestDto userRequestDto = jwtManager.getUserRequestDto(accessToken);
        System.out.println("userRequestDto = " + userRequestDto);
        List<String> collect = userRequestDto.getUserAuthorities().stream().map(userAuthority -> userAuthority.getAuthority())
                .map(Authority::getAuthorityName).collect(Collectors.toList());
        collect.forEach(role-> System.out.println("role = " + role));
        model.addAttribute("collect",collect);
        return "account/notice/all-account";
    }


    @GetMapping("/check/admin/{userId}")
    @ApiOperation("해당 userId가 어드민인지 확인 ")
    @ResponseBody
    public boolean isAdmin(@PathVariable String userId) {
        System.out.println("isAdmin - userId = " + userId);
        return userService.isAdmin(userId);
    }
    @GetMapping("/find/id")
    @ApiOperation("userId 찾기 뷰 반환")
    public String getUserIdView() {
        return "find/findId";
    }
    @GetMapping("/change/password")
    @ApiOperation("userId 찾기 뷰 반환")
    public String getChangingPasswordView() {
        return "change/changePassword";
    }

    @GetMapping("/user/logout")
    @ApiOperation(value = "해당 사용자가 일반 유저가 아닐 경우에만 데이터 가져옴")
    public void logOut(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        cookieManager.makeZeroSecondCookie("accessToken",response);
        cookieManager.makeZeroSecondCookie("refreshToken",response);
    }

    @GetMapping("/user-noneuser/account")
    @ResponseBody
    @ApiOperation(value = "board를 가져올 때 user랑 NoneUser에 맞게 각각 처리")
    public UserIdAndNickname getUserAndNoneUserBoard(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String isExistence = authenticationManager.checkAuthenticationManager(request, response);
        if(isExistence == "null") {

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
    @ResponseBody
    @ApiOperation(value = "해당 사용자가 유저일 때만 데이터 가져옴")
    public UserIdAndNickname getUserDto(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        String isExistence = authenticationManager.checkAuthenticationManager(request, response);
        if(isExistence == "null") {
            return null;
        }
        String accessToken = jwtManager.getAccessToken(request);

        log.info("accessToken = {}",accessToken);


        UserRequestDto userRequestDto = jwtManager.getUserRequestDto(accessToken);
        DefaultMember findUser = userService.findUserByUserId(userRequestDto.getUserId());
        userRequestDto.setNickname(findUser.getNickname());
        return UserIdAndNickname.builder()
                .userId(userRequestDto.getUserId())
                .nickname(userRequestDto.getNickname()).build();
    }

    @GetMapping("/none/user/account")
    @ApiOperation(value = "해당 사용자가 일반 유저가 아닐 경우에만 데이터 가져옴")
    @ResponseBody
    public String getNoneUserDto(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        return cookieManager.getUUidCookie(request);
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
        userManager.getResponseAuthenticationDto(responseAuthenticationDto,userRequestDto);
        responseAuthenticationDto.setRole("권한 에러");
        return responseAuthenticationDto;
    }
    @GetMapping("/home")
    @ApiOperation("account 정보를 알려주는 페이지 반환")
    public String root() {
        return "account/notice/home";
    }


}

