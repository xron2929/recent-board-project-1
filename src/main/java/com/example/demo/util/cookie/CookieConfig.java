package com.example.demo.util.cookie;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CookieConfig {
    @Bean
    public CookieManager cookieManager() {
        return new CookieManager();
    }
}
