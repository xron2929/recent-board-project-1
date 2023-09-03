package com.example.demo.board;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class BoardEditUserDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private String likeCount;
    private String disLikeCount;
    @NonNull
    private boolean isSecret;

}

