package com.example.demo.security.jwt;

import lombok.Getter;

public enum Trans {
    FEMALE("여성"),
    MALE("남성"),
    UNSELECTED("성별을 선택하지 않음");
    @Getter
    private final String trans;
    Trans(String trans) {
        this.trans = trans;
    }
}
