package com.example.demo.user.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {

    @Bean
    public UserAuthorityCheckApi userAuthorityCheckApi() {
        return new UserAuthorityCheckApi();
    }
}
