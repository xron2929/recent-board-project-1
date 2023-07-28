package com.example.demo.board;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor
public class BoardPageCalculator {
    public static Long currentAllPage = 10l;

    @Autowired
    BoardPageCalculator2 boardPageCalculator2;
    public BoardPageApiDTO calculate(Long count,Long currentPageQuantity,Long boardQuantity) {

        long maxCurrentAllBoardQuantity = boardQuantity * 10l;
        long startPageNumber = (currentPageQuantity - 1) / 10 * 10;

        System.out.println("count = " + count);
        long countPageNumber = count / boardQuantity;

        System.out.println("countPageLine = " + countPageNumber);
        System.out.println("startPageNumber = " + startPageNumber);
        if(count == 0) {
            return new BoardPageApiDTO(1l,0l,false,count);
        }
        if(currentPageQuantity >=1l && currentPageQuantity <= 10l) {
            System.out.println(" wtf4 "); // O
            return boardPageCalculator2.calculateOnePage(count,currentPageQuantity, boardQuantity);
        }
        System.out.println(" wtf20 "); // o
        return boardPageCalculator2.calculateNotOnePage(count,currentPageQuantity, boardQuantity);
    }


}

