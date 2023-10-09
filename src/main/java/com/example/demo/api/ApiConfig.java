package com.example.demo.api;

import com.example.demo.cookie.CookieManager;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.user.UserService;
import com.example.demo.user.noneuser.NoneMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApiConfig {

    @Bean
    public MemberApi memberApi() {
        return new MemberApi();
    }
}
