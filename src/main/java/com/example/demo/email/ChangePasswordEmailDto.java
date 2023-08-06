package com.example.demo.email;

import lombok.Getter;

@Getter
public class ChangePasswordEmailDto {
    private String userId;
    private String changePassword;
    private String certificationCode;
    private String email;

    public ChangePasswordEmailDto(String userId, String changePassword, String certificationCode, String email) {
        this.userId = userId;
        this.changePassword = changePassword;
        this.certificationCode = certificationCode;
        this.email = email;
    }
}
