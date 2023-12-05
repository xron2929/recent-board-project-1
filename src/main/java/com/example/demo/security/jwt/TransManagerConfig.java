package com.example.demo.security.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransManagerConfig {
    @Bean
    public TransManager transManager() {
        return new TransManager();
    }
}
