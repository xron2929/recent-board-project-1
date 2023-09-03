package com.example.demo.board;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class BoardEditNoneUserDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private String password;
    private Long boardId;
    private String likeCount;
    private String disLikeCount;
    @NonNull
    private boolean isSecret;
    public BoardEditNoneUserDto(Long id,String title, String content, String username,String password,
                                String likeCount,String disLikeCount, boolean isSecret) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.password = password;
        this.likeCount = likeCount;
        this.disLikeCount = disLikeCount;
        this.isSecret = isSecret;
    }

    public BoardEditNoneUserDto(Long id,String title, String content, String username,String password, Long boardId,
                                String likeCount,String disLikeCount, boolean isSecret) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.password = password;
        this.boardId = boardId;
        this.likeCount = likeCount;
        this.disLikeCount = disLikeCount;
        this.isSecret = isSecret;
    }
}

