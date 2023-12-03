package com.example.demo.boradAndUser;


import lombok.Getter;

@Getter
public class TitleAndUserIdDto {


    public TitleAndUserIdDto(String title, String userId, String password) {
        this.title = title;
        this.userId = userId;
        this.password = password;
    }

    private String title;
    private String userId;
    private String password;

}
