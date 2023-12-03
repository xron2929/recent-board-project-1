package com.example.demo.comment.managerApi;


import com.example.demo.board.BoardService;
import com.example.demo.comment.CommentService;
import com.example.demo.entityjoin.ParentCommentSaveViewDto;
import com.example.demo.boradAndUser.UserJoinRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class CommentApiController {
    @Autowired
    CommentService commentService;
    @Autowired
    BoardService boardService;
    @Autowired
    UserJoinRepository userRepository2;
    @GetMapping("/make/comment/{pathComment}")
    @ApiOperation("해당 boardId에 댓글 등록")
    @ResponseBody
    public String addComment(@RequestParam Long boardId, @PathVariable Long pathComment) throws Exception {
        List<ParentCommentSaveViewDto> parentCommentSaveViewDtos = new ArrayList<>();
        for (int i = 0; i < pathComment; i++) {
            ParentCommentSaveViewDto commentReadViewDto =
                    ParentCommentSaveViewDto.builder()
                            .nickname("sdf")
                            .boardId(boardId)
                            .userId(UUID.randomUUID().toString())
                            .password("sdf")
                            .content("랜덤으로 생성").build();

            //commentService.saveParentComment(commentReadViewDto);
            parentCommentSaveViewDtos.add(commentReadViewDto);
        }
        commentService.saveParentComments(parentCommentSaveViewDtos);
        return "ok";
    }
    @GetMapping("/make/comment/divide/{pathComment}")
    @ApiOperation("모든 BoardId에 parentComment 갯수만큼 나눠서 등록")
    @ResponseBody
    public String addComment(@PathVariable Long pathComment) throws Exception {
        List<ParentCommentSaveViewDto> parentCommentSaveViewDtos = new ArrayList<>();
        Long boardCount = boardService.findBoardOnlyCount();
        for (int i = 0; i < pathComment; i++) {
            long boardId = (i % boardCount)+1l;
            ParentCommentSaveViewDto commentReadViewDto =
                    ParentCommentSaveViewDto.builder()
                            .nickname("sdf")
                            .boardId(boardId)
                            .userId(UUID.randomUUID().toString())
                            .password("sdf")
                            .content("랜덤으로 생성").build();
            //commentService.saveParentComment(commentReadViewDto);
            parentCommentSaveViewDtos.add(commentReadViewDto);
        }
        commentService.saveParentComments(parentCommentSaveViewDtos);
        return "ok";
    }
}

