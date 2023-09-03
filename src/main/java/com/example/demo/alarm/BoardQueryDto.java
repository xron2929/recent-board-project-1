package com.example.demo.alarm;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class BoardQueryDto {
    private int pageQuantity;
    private int boardQuantity;
    @Setter
    private Long startBoardQuantity;

    public BoardQueryDto(int pageQuantity, int boardQuantity) {
        this.pageQuantity = pageQuantity;
        this.boardQuantity = boardQuantity;
    }
}

