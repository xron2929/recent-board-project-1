package com.example.demo.entityjoin;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CommentReadDataDto {
    private String userName;
    private String content;

    public CommentReadDataDto(String userName, String content) {
        this.userName = userName;
        this.content = content;
    }

}

