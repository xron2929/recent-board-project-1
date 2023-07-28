package com.example.demo.board;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BoardPageConfigure {
    @Bean
    BoardPageCalculator boardPageCalculator(){
        return new BoardPageCalculator();
    }
}
