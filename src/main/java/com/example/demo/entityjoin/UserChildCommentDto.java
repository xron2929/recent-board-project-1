package com.example.demo.entityjoin;

import lombok.Data;

@Data
public class UserChildCommentDto {
    String content;
    String parentId;
    String userId;
    public UserChildCommentDto(String content, String userId,String parentId) {
        this.content = content;
        this.userId = userId;
        this.parentId = parentId;
    }
}
