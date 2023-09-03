package com.example.demo.board;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BoardPageCalculator2 {

    public BoardPageApiDTO calculateNotOnePage(Long count,Long currentPageQuantity,Long boardQuantity) {
        long startPageTemp = (currentPageQuantity - 1) / 10 * 10;
        // 10
        long startPage = startPageTemp + 1l;
        // 11
        long endBored = startPageTemp * boardQuantity;
        // 10 * 20 = 200
        long boredTemp = count - endBored;
        // 230 - 200 = 30
        long rest = boredTemp % boardQuantity;
        long page = boredTemp / boardQuantity;
        long finalBoard;
        boolean isNextButton = false;
        if(rest>0) {
            page = page +1;
        }
        // 2
        if(page > 10) {
            isNextButton = true;
            page = 10;
        }
        System.out.println("page = " + page);
        return new BoardPageApiDTO(startPage,page,isNextButton,count);
    }
    public BoardPageApiDTO calculateOnePage(Long count,Long currentPageQuantity,Long boardQuantity) {
        // 41,42,43,44,45,46,47,48,49,50 -> 41
        long page = count / boardQuantity;
        long rest = count % boardQuantity;
        boolean isNextButton = false;
        if(rest > 0) {
            page = page + 1l;
        }
        if(page > 10) {
            isNextButton = true;
            page = 10;
        }
        System.out.println("page = " + page);
        return new BoardPageApiDTO(1l,page,isNextButton,count);
    }
}

