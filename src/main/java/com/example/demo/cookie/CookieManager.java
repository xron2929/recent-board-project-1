package com.example.demo.cookie;


import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

public class CookieManager {

    public void makeSessionSecurityCookie(String key, String value, HttpServletResponse response) {

        Cookie cookie = new Cookie(key,value);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }
    public void makeSecurityCookie(String key, String value,int second, HttpServletResponse response) {

        Cookie cookie = new Cookie(key,value);
        cookie.setMaxAge(second);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    public void makeNotSecurityCookie(String key, String value,int second, HttpServletResponse response) {
        response.setHeader("Access-Control-Max-Age", "0");
        Cookie cookie = new Cookie(key,value);
        cookie.setMaxAge(second);

        cookie.setSecure(true);
        cookie.setHttpOnly(false);
        cookie.setPath("/");

        response.addCookie(cookie);
    }
    public void makeZeroSecondCookie(String key,HttpServletResponse response) {
        response.setHeader("Access-Control-Max-Age", "0");
        Cookie cookie = new Cookie(key,null);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }
    public String getUUidCookie(HttpServletRequest request) {

        if(request.getCookies()== null) {
            return null;
        }
        String uuid = Arrays.stream(request.getCookies())
                .filter(cookie ->cookie.getName().equals("uuid"))
                .map(Cookie::getValue)
                .findFirst().orElse(null);
        return uuid;
    }
}

