package com.example.demo.request;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestIpConfig {
    @Bean
    public RequestIpApi requestIpDto() {
        return new RequestIpApi();
    }
}
