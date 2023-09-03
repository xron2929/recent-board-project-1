package com.example.demo.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserIdAndPasswordDto {
    private String userId;
    private String password;
}
