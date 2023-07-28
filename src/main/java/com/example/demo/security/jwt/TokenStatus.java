package com.example.demo.security.jwt;

public enum TokenStatus {
    TOKEN_ERROR("토큰이 만료되었거나 잘못된 토큰일 경우"),
    TIME_SAFE("토큰 정상 작동"),
    NONE("비회원(토큰 없음)");
    private String tokenMessage;
    TokenStatus(String tokenMessage) {
        this.tokenMessage = tokenMessage;
    }
}

