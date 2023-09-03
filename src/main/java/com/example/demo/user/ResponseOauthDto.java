package com.example.demo.user;


import com.example.demo.security.jwt.Trans;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Data
public class ResponseOauthDto {

    @JsonProperty("phone_number")
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$",
            message = "000-0000-0000 / 000-000-0000 같은 형식으로 입력해야 합니다.")
    private String phoneNumber;
    private long age;
    private String trans;

    private String nickname;

    public long getAge() {
        return age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTrans() {
        return trans;
    }

    public String getNickname() {
        return nickname;
    }
}

