package com.example.demo.board;


import lombok.*;

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
    @Builder
    public BoardEditUserDto(Long id, String title, String content, String username, String likeCount, String disLikeCount, @NonNull boolean isSecret) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.likeCount = likeCount;
        this.disLikeCount = disLikeCount;
        this.isSecret = isSecret;
    }
}

