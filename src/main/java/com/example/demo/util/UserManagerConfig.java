package com.example.demo.util;

import com.example.demo.security.jwt.JwtManager;
import com.example.demo.util.cookie.CookieManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserManagerConfig {
    @Autowired
    JwtManager jwtManager;
    @Autowired
    CookieManager cookieManager;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Bean
    public UserManager userManager() {
        return new UserManager(jwtManager,cookieManager,passwordEncoder);
    }
}
