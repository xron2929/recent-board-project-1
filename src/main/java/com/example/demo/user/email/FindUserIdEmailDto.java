package com.example.demo.user.email;

import lombok.Getter;

@Getter
public class FindUserIdEmailDto {
    private String certificationCode;
    private String email;

    public FindUserIdEmailDto(String certificationCode, String email) {
        this.certificationCode = certificationCode;
        this.email = email;
    }
}
