package com.example.demo.entityjoin;

import com.example.demo.board.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardFindDataDto {

    private Long id;
    private String title;
    private String content;
    private String userId;
    public BoardFindDataDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.userId = board.getMember().getUserId();
    }
}
