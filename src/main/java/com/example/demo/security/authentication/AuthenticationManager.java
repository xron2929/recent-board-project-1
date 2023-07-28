package com.example.demo.security.authentication;

import com.example.demo.cookie.CookieManager;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.UserRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthenticationManager {
    private final Map<String, String> managerUrlMap = new HashMap<>();
    private JwtManager jwtManager;

    @Autowired
    public AuthenticationManager(JwtManager jwtManager, ObjectMapper objectMapper) {
        this.jwtManager = jwtManager;
    }

    public String checkAuthenticationManager(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        log.info("checkAuthenticationManager");


        CookieManager cookieManager = new CookieManager();
        System.out.println("jwtManager = " + jwtManager);
        String accessToken = jwtManager.getAccessToken(request);
        String refreshToken = jwtManager.getRefreshToken(request);

        // log.info("authentication = " + authentication.getCredentials());
        //
        if (refreshToken == null) {
            cookieManager.makeZeroSecondCookie("accessToken", response);
            return "null";
        }
        if (accessToken == null) {
            UserRequestDto userRequestDto = jwtManager.getUserRequestDto(refreshToken);
            jwtManager.setAccessToken(request, response, userRequestDto);
        }
        UserRequestDto userRequestDto1 = jwtManager.getUserRequestDto(accessToken);
        System.out.println("userRequestDto1.getPassword() = " + userRequestDto1.getPassword());
        return accessToken;
    }

    public String getAuthenticationView(HttpServletRequest request, HttpServletResponse response, Map<String, String> managerUrlMap) throws JsonProcessingException {
        String accessToken = checkAuthenticationManager(request, response);
        if (accessToken == "null") {
            return managerUrlMap.get("ROLE_ANONYMOUS");
        }
        // list 권한 목록 가져옴
        // if(list 권한 중에 어드민이 있다면)
        // return 뷰 = managerUrl.get(ROLE_ADMIN);
        // if(list.get(ROLE_USER)!= null)
        // mageUrl.get(ROLE_USER)
        // if(list.get(ROLE_OAUTH)!= null)
        // mageURL.get(ROLE_OAUTH)
        String role = jwtManager.getAuthorityName(accessToken);
        if (role.equals("ROLE_ADMIN")) {
            return managerUrlMap.get(role);
        }
        if (role.equals("ROLE_OAUTH_USER")) {
            return managerUrlMap.get(role);
        }
        if (role.equals("ROLE_SITE_USER")) {
            return managerUrlMap.get(role);
        }
        return managerUrlMap.get("ROLE_ANONYMOUS");
    }
}


