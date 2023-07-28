package com.example.demo.security.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtManagerConfig {
    @Bean
    public JwtManager jwtManager() {
        return new JwtManager();
    }
    @Bean
    public RefreshTokenManager refreshTokenManager() {
        return new RefreshTokenManager();
    }

}
