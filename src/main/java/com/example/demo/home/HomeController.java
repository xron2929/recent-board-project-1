package com.example.demo.home;


import com.example.demo.role.RoleStatus;
import com.example.demo.alarm.BoardCalculator;
import com.example.demo.alarm.BoardQueryDto;
import com.example.demo.board.BoardService;
import com.example.demo.user.MemberBoardQueryDTO;
import com.example.demo.user.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Controller
public class HomeController {
    @Value("${server.domain.url}")
    private String serverDomainUrl;
    @Autowired
    UserService memberService;
    @Autowired
    BoardService boardService;

    @GetMapping("/home")
    @ApiOperation("account 정보를 알려주는 페이지 반환")
    public String root() {
        return "account/notice/home";
    }


    @GetMapping
    @ApiOperation("루트 뷰(게시판 목록들이 포함된)를 반환")
    public String root(@RequestParam(defaultValue = "1") Long pageQuantity, @RequestParam(defaultValue = "20") Long boardQuantity, Model model) {
        model.addAttribute("serverDomainUrl", serverDomainUrl);


        model.addAttribute("jsDomainUrl", serverDomainUrl+"/js/boards/un-search/un-search-script.js");
        model.addAttribute("searchDomainUrl", serverDomainUrl+"/board/search");
        model.addAttribute("naverDomainUrl", serverDomainUrl+"/oauth2/authorization/naver");

        return "boards/un-search/un-search";
    }

    @GetMapping("/boards")
    @ApiOperation("pageQuantity, boardQuantity 바탕으로 boards 데이터 목록을 반환")
    @ResponseBody
    public List<MemberBoardQueryDTO> board(@RequestParam(defaultValue="1") int pageQuantity, @RequestParam(defaultValue="20") int boardQuantity) {
        BoardQueryDto boardQueryDto = new BoardQueryDto(pageQuantity,boardQuantity);
        System.out.println("boardQueryDto = " + boardQueryDto);
        BoardCalculator boardCalculator = new BoardCalculator();
        boardCalculator.calculate(boardQueryDto);
        List<MemberBoardQueryDTO> boards = memberService.findBoards(boardQueryDto.getStartBoardQuantity(),boardQueryDto.getBoardQuantity());
        for (MemberBoardQueryDTO board:boards){
            System.out.println("board.getId() = " + board.getBoardId());
            System.out.println("board.getContents() = " + board.getContents());
            System.out.println("board.getTitle() = " + board.getTitle());
            System.out.println("board.getUserName() = " + board.getUserName());
        }
        return boards;
    }




}

