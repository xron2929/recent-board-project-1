package com.example.demo.alarm;


import org.springframework.beans.factory.annotation.Autowired;

public class PageCalculator {
    private Long currentPage;
    private String userId;
    private Long boardQuantity;
    private Long dataSize;
    @Autowired
    AlarmService alarmService;

    public PageCalculator(String userId,Long currentPage,Long boardQuantity,Long dataSize) {
        this.currentPage = currentPage;
        this.userId = userId;
        this.boardQuantity = boardQuantity;
        this.dataSize = dataSize;
    }

    public PageDto getPage() {
        System.out.println("PageController - getPage() currentPage = " + currentPage);
        System.out.println("PageController - dataSize() dataSize = " + dataSize);

        long startPageTemp = (currentPage - 1) / 10 * 10;
        long startPage = startPageTemp + 1l;
        long allDataPage = 0;
        if(dataSize% boardQuantity>0) {
            allDataPage = 1;
        }
        allDataPage = allDataPage + (dataSize / boardQuantity);
        if(allDataPage - startPage > 10) {
            // 시작 , 다음누른 후 페이지, 마지막 페이지
            PageDto pageDto = PageDto.builder().isNextPage(true)
                    .tenFirstPageNum(startPage)
                    .tenFinalPageNum(startPage+10l)
                    .allFinalPageNum(allDataPage)
                    .build();
            return pageDto;
        }
        if(allDataPage - startPage == 10 ) {
            PageDto pageDto = PageDto.builder().isNextPage(false)
                    .tenFirstPageNum(startPage)
                    .tenFinalPageNum(startPage+10l)
                    .allFinalPageNum(allDataPage)
                    .build();
            return pageDto;
        }
        // 2 - 1 = 1 page
        PageDto pageDto = PageDto.builder().isNextPage(false)
                .tenFirstPageNum(startPage)
                .tenFinalPageNum(allDataPage)
                .allFinalPageNum(allDataPage)
                .build();
        return pageDto;
    }


}

