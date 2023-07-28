package com.example.demo.board;


import lombok.Data;

@Data
public class BoardPageApiDTO {
    private Long pageQuantity;
    private Boolean isNextButton;
    private Long startNumber;
    private Long currentPageFinalBoardQuantity;
    public BoardPageApiDTO(Long pageQuantity, Boolean isNextButton) {
        this.pageQuantity = pageQuantity;
        this.isNextButton = isNextButton;
    }

    public BoardPageApiDTO(Long startNumber,Long pageQuantity, Boolean isNextButton,Long currentPageFinalBoardQuantity) {
        this.startNumber = startNumber;
        this.pageQuantity = pageQuantity;
        this.isNextButton = isNextButton;
        this.currentPageFinalBoardQuantity = currentPageFinalBoardQuantity;
    }
}
