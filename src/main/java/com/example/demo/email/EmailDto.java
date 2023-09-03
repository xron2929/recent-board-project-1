package com.example.demo.email;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailDto {
    private String email;
    public EmailDto(String email) {
        this.email = email;
    }
}
