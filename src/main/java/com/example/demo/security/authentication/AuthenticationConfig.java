package com.example.demo.security.authentication;

import com.example.demo.security.jwt.JwtManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationConfig {

    @Autowired
    JwtManager jwtManager;
    @Bean
    public AuthenticationManager authenticationManager() {
        return new AuthenticationManager(jwtManager);
    }
}

