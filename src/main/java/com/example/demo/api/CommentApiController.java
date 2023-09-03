package com.example.demo.api;


import com.example.demo.board.BoardService;
import com.example.demo.comment.CommentService;
import com.example.demo.entityjoin.CommentSaveViewDto;
import com.example.demo.user.UserJoinRepository;
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
        List<CommentSaveViewDto> commentSaveViewDtos = new ArrayList<>();
        for (int i = 0; i < pathComment; i++) {
            CommentSaveViewDto commentReadViewDto =
                    new CommentSaveViewDto("sdf",boardId, UUID.randomUUID().toString(),"sdfsd");
            //commentService.saveParentComment(commentReadViewDto);
            commentSaveViewDtos.add(commentReadViewDto);
        }
        commentService.saveParentComments(commentSaveViewDtos);
        return "ok";
    }
    @GetMapping("/make/comment/divide/{pathComment}")
    @ApiOperation("모든 BoardId에 parentComment 갯수만큼 나눠서 등록")
    @ResponseBody
    public String addComment(@PathVariable Long pathComment) throws Exception {
        List<CommentSaveViewDto> commentSaveViewDtos = new ArrayList<>();
        Long boardCount = boardService.findBoardOnlyCount();
        for (int i = 0; i < pathComment; i++) {
            long boardId = (i % boardCount)+1l;
            CommentSaveViewDto commentReadViewDto =
                    new CommentSaveViewDto("sdf",boardId, UUID.randomUUID().toString(),"sdfsd");
            //commentService.saveParentComment(commentReadViewDto);
            commentSaveViewDtos.add(commentReadViewDto);
        }
        commentService.saveParentComments(commentSaveViewDtos);
        return "ok";
    }
}

