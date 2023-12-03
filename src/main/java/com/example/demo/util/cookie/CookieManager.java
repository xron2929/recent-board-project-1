package com.example.demo.util.cookie;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

public class CookieManager {



    public void makeSessionSecurityCookie(String key, String value, HttpServletResponse response) {
    /*
        Cookie cookie = new Cookie(key,value);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);

     */
        ResponseCookie cookie = ResponseCookie.from(key, value)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
    public void makeSecurityCookie(String key, String value,int second, HttpServletResponse response) {
    /*
        Cookie cookie = new Cookie(key,value);
        cookie.setMaxAge(second);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

     */
        ResponseCookie cookie = ResponseCookie.from(key, value)
                .path("/")
                .sameSite("None")
                .maxAge(second)
                .httpOnly(true)
                .secure(true)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
    public void makeNotSecurityCookie(String key, String value,int second, HttpServletResponse response) {
        /*
        response.setHeader("Access-Control-Max-Age", "0");
        Cookie cookie = new Cookie(key,value);
        cookie.setMaxAge(second);

        cookie.setSecure(true);
        cookie.setHttpOnly(false);
        cookie.setPath("/");

        response.addCookie(cookie);

         */
        ResponseCookie cookie = ResponseCookie.from(key, value)
                .path("/")
                .sameSite("None")
                .maxAge(second)
                .httpOnly(true)
                .secure(true)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
    public void makeZeroSecondCookie(String key,HttpServletResponse response) {
        /*
        response.setHeader("Access-Control-Max-Age", "0");
        Cookie cookie = new Cookie(key,null);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);

         */
        ResponseCookie cookie = ResponseCookie.from(key, null)
                .path("/")
                .sameSite("None")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public String getUUidCookie(HttpServletRequest request) {

        if(request.getCookies()== null) {
            System.out.println("CookieManager.getUUidCookie - request.getCookies() = null ");
            return null;
        }
        String uuid = Arrays.stream(request.getCookies())
                .filter(cookie ->cookie.getName().equals("uuid"))
                .map(Cookie::getValue)
                .findFirst().orElse(null);
        return uuid;
    }
}

