package com.example.demo.util;

public enum ValidationStatus {
    // 1. 유효하지 않음
    // 2. NoneUser임
    // 3. OauthUser임
    // 4. SiteUser임
    // 5. Admin임
    ERROR_ACCOUNT("등록한 계정 정보와 다르거나 없는 계정"),
    NONE_USER_ACCOUNT("정상적인 비 회원 계정"),
    USER_ACCOUNT("정상적인 일반 회원 계정 또는 어드민 계정"),
    UN_CHECK_USER_ACCOUNT("비회원은 아니며, 시도한 정보가 등록한 계정 정보와 다를 가능성도 존재함");

    private String tokenMessage;
    ValidationStatus(String tokenMessage) {
        this.tokenMessage = tokenMessage;
    }


}
