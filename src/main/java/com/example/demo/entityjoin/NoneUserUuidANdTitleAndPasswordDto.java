package com.example.demo.entityjoin;


import lombok.Getter;

@Getter
public class NoneUserUuidANdTitleAndPasswordDto {
    public NoneUserUuidANdTitleAndPasswordDto(String uuid,String title, String userId,String password) {
        this.uuid = uuid;
        this.title = title;
        this.userId = userId;
        this.password = password;

    }
    private String uuid;
    private String title;
    private String userId;
    private String password;
}

