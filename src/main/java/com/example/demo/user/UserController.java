package com.example.demo.user;

import com.example.demo.alarm.AdditionalInformationRedisTemplate;
import com.example.demo.authority.Authority;
import com.example.demo.security.authentication.AuthenticationManager;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.UserRequestDto;
import com.example.demo.user.oauthuser.OauthUserFirstJoinDto;
import com.example.demo.userAuthority.UserAuthorityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.Keys;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
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


}
