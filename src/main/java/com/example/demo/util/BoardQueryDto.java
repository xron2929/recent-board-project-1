package com.example.demo.util;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class BoardQueryDto {
    private int pageQuantity;
    private int boardQuantity;
    private Long prevBoardQuantity;
    @Builder
    public BoardQueryDto(int pageQuantity, int boardQuantity, Long prevBoardQuantity) {
        this.pageQuantity = pageQuantity;
        this.boardQuantity = boardQuantity;
        this.prevBoardQuantity = prevBoardQuantity;
    }
}

