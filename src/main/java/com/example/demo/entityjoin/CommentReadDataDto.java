package com.example.demo.entityjoin;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CommentReadDataDto {
    private String userId;
    private String content;
    private String nickname;

    public CommentReadDataDto(String userId, String content,String nickname) {
        this.userId = userId;
        this.content = content;
        this.nickname = nickname;
    }

}

