package com.example.demo.entityjoin;


import com.example.demo.comment.ChildComment;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CommentReadDataDto {
    private String userId;
    private String content;
    private String nickname;

    public CommentReadDataDto(ChildComment childComment) {
        this.userId = childComment.getUserId();
        this.nickname = childComment.getAuthorName();
        this.content = childComment.getContents();
    }

}

