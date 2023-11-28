package com.example.demo.entityjoin;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class ParentCommentAndChildCommentDataDto {
    private String parentCommentUserId;
    private String parentCommentContent;
    private String parentCommentNickname;
    private String childCommentUserId;
    private String childCommentContent;
    private String childCommentNickname;

    public ParentCommentAndChildCommentDataDto(String parentCommentUserId, String parentCommentContent, String parentCommentNickname, String childCommentUserId, String childCommentContent, String childCommentNickname) {
        this.parentCommentUserId = parentCommentUserId;
        this.parentCommentContent = parentCommentContent;
        this.parentCommentNickname = parentCommentNickname;
        this.childCommentUserId = childCommentUserId;
        this.childCommentContent = childCommentContent;
        this.childCommentNickname = childCommentNickname;
    }
}
