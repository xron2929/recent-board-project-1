package com.example.demo.security.interceptor;


import com.example.demo.util.cookie.CookieManager;
import com.example.demo.security.jwt.JwtManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LogOutInterceptor implements HandlerInterceptor {

    @Autowired
    CookieManager cookieManager;
    @Autowired
    JwtManager jwtManager;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        cookieManager.makeZeroSecondCookie("accessToken",response);
        cookieManager.makeZeroSecondCookie("refreshToken",response);
        return true;
    }
}
