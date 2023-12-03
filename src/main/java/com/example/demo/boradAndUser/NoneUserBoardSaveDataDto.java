package com.example.demo.boradAndUser;


import com.example.demo.board.Board;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class NoneUserBoardSaveDataDto {
    private Long id;
    private String userId;
    private String title;
    private String content;
    private String nickname;
    private String password;
    private boolean isSecret;
    @Builder
    @NonNull
    public NoneUserBoardSaveDataDto(Long id,String userId, String title, String content, String nickname, String password,boolean isSecret) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.password = password;
        this.isSecret = isSecret;
    }

    public NoneUserBoardSaveDataDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.nickname = board.getMember().getNickname();
        this.password = board.getMember().getPassword();
        this.isSecret = board.isSecret();
    }
    public static NoneUserBoardSaveDataDto of(Board board) {
        return new NoneUserBoardSaveDataDto(board);
    }

}
