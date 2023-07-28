package com.example.demo.security.interceptor;


import com.example.demo.cookie.CookieManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Component
public class DomainUrlInterceptor implements HandlerInterceptor {
    @Value("${domain.url}")
    private String domainUrl;
    @Autowired
    CookieManager cookieManager;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("domainUrl = " + domainUrl);
        System.out.println("//");
        if(request.getCookies() == null) {
            cookieManager.makeNotSecurityCookie("domainUrl", domainUrl,7 * 24 * 60 * 60,response);
            System.out.println("??DS?ffsd2");
            return true;
        }
        System.out.println("??DS?ffsd");
        System.out.println("request = " + request);
        boolean isDomainUrl = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("domainUrl"))
                .map(cookie -> cookie.getName()).findFirst().isPresent();
        System.out.println("isDomainUrl = " + isDomainUrl);
        if(isDomainUrl == false) {
            cookieManager.makeNotSecurityCookie("domainUrl", domainUrl,7 * 24 * 60 * 60,response);
        }
        return true;
    }
}

