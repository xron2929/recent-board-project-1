package com.example.demo.security.interceptor;


import com.example.demo.cookie.CookieManager;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.TokenStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Value("${domain.url}")
    private String domainUrl;
    @Autowired
    CookieManager cookieManager;
    @Autowired
    JwtManager jwtManager;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String refreshToken = jwtManager.getRefreshToken(request);
        TokenStatus isSafeJwt = jwtManager.validaition(refreshToken);
        if(isSafeJwt == TokenStatus.TOKEN_ERROR) {
            cookieManager.makeZeroSecondCookie("accessToken",response);
            cookieManager.makeZeroSecondCookie("refreshToken",response);
            response.sendRedirect("/login");
            return false;
        }
        if(isSafeJwt == TokenStatus.NONE) {
            String uuidCookie = cookieManager.getUUidCookie(request);
            if(uuidCookie != null) {
                return true;
            }
            cookieManager.makeSessionSecurityCookie("uuid", UUID.randomUUID().toString(),response);
        }
        return true;

    }
}

