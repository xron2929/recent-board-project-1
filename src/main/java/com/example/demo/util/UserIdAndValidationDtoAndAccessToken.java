package com.example.demo.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class UserIdAndValidationDtoAndAccessToken {
    private ValidationStatus validationStatus;
    private String userId;
    private String accessToken;

    static UserIdAndValidationDtoAndAccessToken createUserIdAndValidationDto(ValidationStatus validationStatus, String userId) {
        return new UserIdAndValidationDtoAndAccessToken(validationStatus,userId);
    }
    static UserIdAndValidationDtoAndAccessToken createUserIdAndValidationDtoAndAccessToken(ValidationStatus validationStatus,String userId,String accessToken) {
        return new UserIdAndValidationDtoAndAccessToken(validationStatus,userId,accessToken);
    }

    private UserIdAndValidationDtoAndAccessToken(ValidationStatus validationStatus, String userId, String accessToken) {
        this.validationStatus = validationStatus;
        this.userId = userId;
        this.accessToken = accessToken;
    }

    private UserIdAndValidationDtoAndAccessToken(ValidationStatus validationStatus, String userId) {
        this.validationStatus = validationStatus;
        this.userId = userId;
    }
}
