package com.example.demo.user.oauthuser;

import com.example.demo.security.jwt.Trans;
import lombok.Getter;

@Getter
public class OauthUserFirstJoinDto {
    private Long age;
    private String phoneNumber;
    private String trans;

    public OauthUserFirstJoinDto(Long age, String phoneNumber, String trans) {
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.trans = trans;
    }
}
