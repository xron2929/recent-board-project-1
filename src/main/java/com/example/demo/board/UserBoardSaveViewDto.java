package com.example.demo.board;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class UserBoardSaveViewDto {

    private Long boardId;

    private String title;
    private String content;
    private boolean isSecret;

    @Builder
    public UserBoardSaveViewDto(Long boardId,String title, String content, String username, String password, boolean isSecret) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.isSecret = isSecret;
    }

    public UserBoardSaveViewDto(Board board) {
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.isSecret = board.isSecret();
    }
}


