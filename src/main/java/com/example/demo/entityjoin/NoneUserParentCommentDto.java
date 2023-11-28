package com.example.demo.entityjoin;

import lombok.Getter;
@Getter
public class NoneUserParentCommentDto {

    String content;
    String nickname;
    String password;
    public NoneUserParentCommentDto(String nickname,String content,String password) {
        this.nickname = nickname;
        this.content = content;
        this.password = password;
    }
}
