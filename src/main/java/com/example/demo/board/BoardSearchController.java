package com.example.demo.board;


import com.example.demo.entityjoin.BoardSearchDataDto;
import com.example.demo.log.Trace;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class BoardSearchController {
    @Autowired
    BoardSearchService boardSearchService;

    @GetMapping("/board/search")
    @ApiOperation("board searchScript 뷰 반환")
    // @Trace
    public String view2(BoardSearchDTO boardSearchDTO) {
        System.out.println("boardSearchDTO.getKeyword() = " + boardSearchDTO.getKeyword());
        System.out.println("boardSearchDTO.getBoardQuantity() = " + boardSearchDTO.getBoardQuantity());
        System.out.println("boardSearchDTO.getPageQuantity() = " + boardSearchDTO.getPageQuantity());
        return "boards/search/search";
    }
    @GetMapping("/board/data")
    @ApiOperation("해당 검색어에 맞는 BOARD 데이터들 반환")
    @ResponseBody
    @Trace
    List<BoardSearchDataDto> searchFindBoard(BoardSearchDTO boardSearchDTO) {
        List<BoardSearchDataDto> boardSearchDataDtos = boardSearchService.boardSearch(boardSearchDTO.getPageQuantity(), boardSearchDTO.getBoardQuantity(),
                boardSearchDTO.getKeyword());
        System.out.println("boardSearchDataDtos.size() = " + boardSearchDataDtos.size());
        return boardSearchDataDtos;
    }
    @GetMapping("/board/data/count")
    @ResponseBody
    @ApiOperation("다음에 나타날 페이지 갯수, 다음 페이지랑 이전 페이지 번호 반환")
    @Trace
    BoardPageApiDTO searchFindBoardCount(BoardSearchDTO boardSearchDTO) {
        return boardSearchService.boardSearchPage(boardSearchDTO.getPageQuantity(),boardSearchDTO.getBoardQuantity(),boardSearchDTO.getKeyword());
    }
    @Data
    static class BoardSearchDTO {
        private String keyword;
        private Long boardQuantity;
        private Long pageQuantity;
    }
}

