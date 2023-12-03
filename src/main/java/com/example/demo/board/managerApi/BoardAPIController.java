package com.example.demo.board.managerApi;

import com.example.demo.role.RoleStatus;
import com.example.demo.board.Board;
import com.example.demo.board.BoardPageApiDTO;
import com.example.demo.board.BoardService;
import com.example.demo.util.cookie.CookieManager;
import com.example.demo.log.Trace;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.TokenStatus;
import com.example.demo.user.UserService;
import com.example.demo.user.defaultuser.DefaultMember;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BoardAPIController {
    // 마지막 Board ID API 가져오기
    @Autowired
    BoardService boardService;
    @Autowired
    UserService userService;
    @Autowired
    JwtManager jwtManager;
    @Autowired
    CookieManager cookieManager;
    @GetMapping("/make/board/{boardQuantity}")
    @ResponseBody
    @ApiOperation("board 생성")
    @Trace
    public String addBoard(@PathVariable Long boardQuantity, HttpServletRequest request) throws Exception {
        String refreshToken = jwtManager.getRefreshToken(request);
        TokenStatus tokenStatus = jwtManager.validation(refreshToken);
        if(!tokenStatus.equals(TokenStatus.TIME_SAFE)) {
            return "토큰 에러";
        }
        String authorityName = jwtManager.getAuthorityName(refreshToken);
        if(!authorityName.equals(RoleStatus.ROLE_ADMIN.name())) {
            return "권환 부족";
        }
        DefaultMember member = DefaultMember.builder().userId("sdfds").password("dsfds").build();
        userService.saveUser(member);
        List<Board> boards = new ArrayList<>();
        for (int i = 1; i <= boardQuantity; i++) {
            boards.add(new Board(member,String.valueOf(i),"임시 생성",false));
        }
        boardService.saveAll(boards);
        return "ok";
    }

    @GetMapping("/page")
    @ApiOperation("boardPage에서 다음 버튼 눌렀을 때 번호, 시작 버튼 눌렀을 때 번호, page를 눌렀을 때 번호")
    @ResponseBody
    public BoardPageApiDTO view(@RequestParam Long currentBoardPage
            ,@RequestParam Long boardQuantity) {
        BoardPageApiDTO boardCount = boardService.findBoardCount(currentBoardPage, boardQuantity);
        System.out.println("boardCount.getIsNextButton() = " + boardCount.getIsNextButton());
        System.out.println("boardCount.getStartNumber() = " + boardCount.getStartNumber());
        System.out.println("boardCount.getPageQuantity() = " + boardCount.getPageQuantity());
        return boardCount;
    }

    @Data
    @AllArgsConstructor
    static class FinalBoardIdDto {
        private Long id;
    }
    @GetMapping("/board/uuid")
    @ApiOperation("none_user의 알림 서비스의 경우 UUID를 기반으로 확인을 하는데, 댓글을 어떤 사람이 달았던 간에, " +
            "해당 글쓴이가 비회원인 경우, 해당 비회원인 글 작성자에게 알림 서비스를 제공하기 위해 누가 글을 쓰면 그 boardID를 기준으로 UUID값을 가져감")
    @ResponseBody
    public String getUserAndNoneUserBoard(HttpServletRequest request, HttpServletResponse response, @RequestParam Long boardId) throws Exception {
        return boardService.findBoardUserUUID(boardId);
    }
}
