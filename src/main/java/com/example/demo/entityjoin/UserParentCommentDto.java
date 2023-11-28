package com.example.demo.entityjoin;

import lombok.Getter;

@Getter
public class UserParentCommentDto {
    String content;
    String userId;
    public UserParentCommentDto(String content, String userId) {
        this.content = content;
        this.userId = userId;
    }
}
