package com.example.demo.board;


import com.example.demo.entityjoin.BoardSearchDataDto;
import com.example.demo.log.Trace;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
        System.out.println(" ??? view2");
        System.out.println("boardSearchDTO.getKeyword() = " + boardSearchDTO.getKeyword());
        System.out.println("boardSearchDTO.getBoardQuantity() = " + boardSearchDTO.getBoardQuantity());
        System.out.println("boardSearchDTO.getPageQuantity() = " + boardSearchDTO.getPageQuantity());
        return "boards/search/search";
    }
    @GetMapping("/board/data")
    @ApiOperation("해당 검색어에 맞는 BOARD 데이터들 반환")
    @ResponseBody
    @Trace
    List<BoardSearchDataDto> searchFindBoard(@RequestParam Long pageQuantity, @RequestParam Long boardQuantity, @RequestParam String keyword) {
        System.out.println(" ??? searchFindBoard");
        List<BoardSearchDataDto> boardSearchDataDtos = boardSearchService.boardSearch( pageQuantity,boardQuantity,keyword);
        System.out.println("boardSearchDataDtos.size() = " + boardSearchDataDtos.size());
        return boardSearchDataDtos;
    }
    @GetMapping("/board/data/count")
    @ResponseBody
    @ApiOperation("다음에 나타날 페이지 갯수, 다음 페이지랑 이전 페이지 번호 반환")
    @Trace
    BoardPageApiDTO searchFindBoardCount(@RequestParam Long pageQuantity,@RequestParam Long boardQuantity,@RequestParam String keyword) {
        return boardSearchService.boardSearchPage( pageQuantity,boardQuantity,keyword);
    }
    @Data
    static class BoardSearchDTO {
        private String keyword;
        private Long boardQuantity;
        private Long pageQuantity;
    }
}

