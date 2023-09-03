package com.example.demo.api;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
public class BoardRequestDto {
    private Long id;
    private String username;
    private String password;
    private String title;
    private String content;
    private String likeCount;
    private String disLikeCount;
    @NonNull
    private boolean isSecret;
    // ROLE_USER랑 JWT_USER 대상만 복구 가능한 컨트롤러 제작
    public BoardRequestDto(Long id,String username, String password, String title, String content, boolean isSecret) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.title = title;
        this.content = content;
        this.isSecret = isSecret;
    }
    public BoardRequestDto(String username, String password, String title, String content, boolean isSecret) {
        this.username = username;
        this.password = password;
        this.title = title;
        this.content = content;
        this.isSecret = isSecret;
    }

    public BoardRequestDto( String username, String password, String title, String content,
                            String likeCount, String disLikeCount, boolean isSecret) {
        this.username = username;
        this.password = password;
        this.title = title;
        this.content = content;
        this.likeCount = likeCount;
        this.disLikeCount = disLikeCount;
        this.isSecret = isSecret;
    }
}

