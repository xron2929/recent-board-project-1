package com.example.demo.entityjoin;

import lombok.Data;
@Data
public class NoneUserChildCommentDto {
    private String password;
    private String nickname;
    private String content;
    private String parentId;
    private String userId;
    public NoneUserChildCommentDto(String password, String nickname,String content, String userId,String parentId) {
        this.content = content;
        this.userId = userId;
        this.parentId = parentId;
        this.password = password;
        this.nickname = nickname;
    }
}


