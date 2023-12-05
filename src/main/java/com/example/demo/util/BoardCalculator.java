package com.example.demo.util;


public class BoardCalculator {
        // 현재 20개 가져오고
    public BoardQueryDto calculate(int pageQuantity,int boardQuantity) {
        // 카운트 쿼리 가져오고
        // 얘네 DTO로 묶기
        BoardQueryDto boardQueryDto = BoardQueryDto.builder()
                .prevBoardQuantity((pageQuantity - 1l) * boardQuantity)
                .boardQuantity(boardQuantity)
                .pageQuantity(pageQuantity)
                .build();


        return boardQueryDto;
    }
}
