package com.example.demo.entityjoin;


import com.example.demo.board.Board;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class NoneUserBoardSaveDataDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private String password;
    private boolean isSecret;
    @Builder
    @NonNull
    public NoneUserBoardSaveDataDto(Long id, String title, String content, String username, String password,boolean isSecret) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.password = password;
        this.isSecret = isSecret;
    }

    public NoneUserBoardSaveDataDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.username = board.getMember().getUserId();
        this.password = board.getMember().getPassword();
        this.isSecret = board.isSecret();
    }
    public static NoneUserBoardSaveDataDto of(Board board) {
        return new NoneUserBoardSaveDataDto(board);
    }

}
