package com.example.demo.comment;


import com.example.demo.api.UserApiController;
import com.example.demo.board.Board;
import com.example.demo.board.BoardService;
import com.example.demo.entityjoin.CommentReadDataDto;
import com.example.demo.entityjoin.CommentReadViewDto;
import com.example.demo.entityjoin.CommentSaveViewDto;
import com.example.demo.entityjoin.UserAndBoardDto;
import com.example.demo.user.UserService;
import com.example.demo.user.defaultuser.DefaultMember;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    BoardService boardService;
    @Autowired CommentMapper commentMapper;
    @PostMapping("/comment/{boardId}")
    @ApiOperation("해당 BoardId에 comment 등록함")
    @ResponseBody
    public SuccessDto getBoard(@PathVariable Long boardId, @RequestBody CommentSaveViewDto commentDto,HttpServletRequest request) throws Exception {

        commentDto.setBoardId(boardId);
        // 10개 데이터 불러오기
        commentService.saveParentComment(commentDto,request);

        return new SuccessDto("ok");
    }

    @Data
    static class UserCommentDto {
        String content;
        String userId;
        public UserCommentDto(@JsonProperty("content")String content, @JsonProperty("userId") String userId) {
            this.content = content;
            this.userId = userId;
        }
    }
    @PostMapping("user/comment/{boardId}")
    @ApiOperation(value = "user에 맞는 comment 등록함")
    @ResponseBody
    public String getUserDto2(@PathVariable Long boardId, @RequestBody UserCommentDto userCommentDto, HttpServletRequest request) throws Exception {
        System.out.println("userId = " + userCommentDto.getUserId());
        System.out.println("content = " + userCommentDto.getContent());
        DefaultMember user = userService.findUserByUserId(userCommentDto.getUserId());
        Board board = boardService.findAllByImageId(boardId);
        UserAndBoardDto userAndBoardDto = new UserAndBoardDto(board,user);
        System.out.println("UserApiController - defaultMember = " + userAndBoardDto);
        commentService.saveOnlyParentComment(userCommentDto.getContent(),userAndBoardDto);
        return "ok";
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class SuccessDto {
        private String response;
    }
    @GetMapping("/comment/{boardId}")
    @ApiOperation("해당 BoardId에 먼저 등록된 것부터 comment 10개씩 가져옴")
    @ResponseBody
    public List<CommentReadDataDto> getBoard(@PathVariable Long boardId, @RequestParam Long startId) throws Exception {
        CommentReadViewDto commentDto = new CommentReadViewDto();
        commentDto.setBoardId(boardId);
        commentDto.setStartId(startId-1);
        List<CommentReadDataDto> commentReadDataDtos = commentService.readTenParentComment(commentDto);
        System.out.println("commentReadDataDtos.size() = " + commentReadDataDtos.size());
        commentReadDataDtos.forEach(commentReadDataDto -> {
                    System.out.println("commentReadDataDto.getNickname() = " + commentReadDataDto.getNickname());
                    System.out.println("commentReadDataDto.getContent() = " + commentReadDataDto.getContent());
                }
        );
        return commentReadDataDtos;
    }

}

