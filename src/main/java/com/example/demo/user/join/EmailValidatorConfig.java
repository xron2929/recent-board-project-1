package com.example.demo.user.join;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailValidatorConfig {
    @Bean
    public EmailValidator emailValidator() {
        return new EmailValidator();
    }
}
