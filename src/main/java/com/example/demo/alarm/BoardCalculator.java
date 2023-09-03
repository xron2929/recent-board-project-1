package com.example.demo.alarm;


import org.springframework.beans.factory.annotation.Autowired;
public class BoardCalculator {
    public BoardQueryDto calculate(BoardQueryDto boardQueryDto) {
        // 현재 20개 가져오고
        // 카운트 쿼리 가져오고
        // 얘네 DTO로 묶기
        int boardQuantity = boardQueryDto.getBoardQuantity();
        int pageQuantity = boardQueryDto.getPageQuantity();
        boardQueryDto.setStartBoardQuantity((pageQuantity - 1l) * boardQuantity);
        return boardQueryDto;
    }
}
