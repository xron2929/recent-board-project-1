package com.example.demo.comment;


import com.example.demo.boradAndUser.UserAndBoardDto;
import com.example.demo.user.authority.Authority;
import com.example.demo.board.Board;
import com.example.demo.board.BoardService;
import com.example.demo.util.cookie.CookieManager;
import com.example.demo.entityjoin.*;
import com.example.demo.util.request.RequestIpApi;
import com.example.demo.role.RoleStatus;
import com.example.demo.user.UserService;
import com.example.demo.user.defaultuser.DefaultMember;
import com.example.demo.user.noneuser.NoneMember;
import com.example.demo.user.userAuthority.UserAuthority;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    @PostMapping("/parentComment/{boardId}")
    @ApiOperation("해당 BoardId에 parentComment 등록함")
    @ResponseBody
    public SuccessDto saveParentCommentNoneUser(@PathVariable Long boardId, @RequestBody ParentCommentSaveViewDto commentDto, HttpServletRequest request) throws Exception {

        commentDto.setBoardId(boardId);
        // 10개 데이터 불러오기
        commentService.saveParentComment(commentDto,request);

        return new SuccessDto("ok");
    }

    @PostMapping("/childComment/{boardId}")
    @ApiOperation("해당 BoardId에 comment 등록함")
    @ResponseBody
    public SuccessDto saveChildCommentNoneUser(@PathVariable Long boardId, @RequestBody ChildCommentSaveViewDto commentDto, HttpServletRequest request) throws Exception {

        commentDto.setBoardId(boardId);
        // 10개 데이터 불러오기
        commentService.saveChildComment(commentDto,request);

        return new SuccessDto("ok");
    }



    @PostMapping("user/parentComment/{boardId}")
    @ApiOperation(value = "user에 맞는 parentComment 등록함")
    @ResponseBody
    public String saveParenctCommentByUser(@PathVariable Long boardId, @RequestBody UserParentCommentDto userParentCommentDto, HttpServletRequest request) throws Exception {
        System.out.println("userId = " + userParentCommentDto.getUserId());
        System.out.println("content = " + userParentCommentDto.getContent());
        DefaultMember user = userService.findUserByUserId(userParentCommentDto.getUserId());
        Board board = boardService.findAllByImageId(boardId);
        UserAndBoardDto userAndBoardDto = new UserAndBoardDto(board,user);
        System.out.println("UserApiController - defaultMember = " + userAndBoardDto);
        commentService.saveOnlyParentComment(userParentCommentDto.getContent(),userAndBoardDto);
        return "ok";
    }
    @PostMapping("user/childComment/{boardId}")
    @ApiOperation(value = "user에 맞는 childComment 등록함")
    @ResponseBody
    public String saveChildCommentByUser(@PathVariable Long boardId, @RequestBody UserChildCommentDto userChildCommentDto, HttpServletRequest request) throws Exception {
        System.out.println("userId = " + userChildCommentDto.getUserId());
        System.out.println("content = " + userChildCommentDto.getContent());
        DefaultMember user = userService.findUserByUserId(userChildCommentDto.getUserId());
        Board board = boardService.findAllByImageId(boardId);
        // ParentComment parentComment = commentService.getParentComment(userChildCommentDto.getParentId());
        UserAndBoardAndParentCommentDto userAndBoardAndParentCommentDto = new UserAndBoardAndParentCommentDto(board,user,new ObjectId(userChildCommentDto.getParentId()));
        System.out.println("UserApiController - defaultMember = " + userAndBoardAndParentCommentDto);
        commentService.saveOnlyChildComment(userChildCommentDto.getContent(),userAndBoardAndParentCommentDto);
        return "ok";
    }

    @PostMapping("noneUser/parentComment/{boardId}")
    @ApiOperation(value = "user에 맞는 parentComment 등록함")
    @ResponseBody
    public String saveParenctCommentByNoneUser(@PathVariable Long boardId,
                                               @RequestBody NoneUserParentCommentDto noneUserParentCommentDto,
                                               HttpServletRequest request) throws Exception {

        System.out.println("content = " + noneUserParentCommentDto.getContent());
        List<UserAuthority> userAuthorities = new ArrayList<>();
        userAuthorities.add(new UserAuthority(new Authority(RoleStatus.ROLE_ANONYMOUS.name())));
        CookieManager cookieManager = new CookieManager();
        NoneMember user = NoneMember.builder()
                .nickname(noneUserParentCommentDto.getNickname())
                .userId(cookieManager.getUUidCookie(request))
                .password(noneUserParentCommentDto.getPassword())
                .ip(request.getRemoteAddr())
                 // .ip(requestIpApi.getClientIpAddr(request))
                .userAuthorities(userAuthorities)
                .build();
        userService.userAndUserAuthoritySave(user);
        Board board = boardService.findAllByImageId(boardId);
        UserAndBoardDto userAndBoardDto = new UserAndBoardDto(board,user);
        System.out.println("UserApiController - defaultMember = " + userAndBoardDto);
        commentService.saveOnlyParentComment(noneUserParentCommentDto.getContent(),userAndBoardDto);
        return "ok";
    }
    @PostMapping("noneUser/childComment/{boardId}")
    @ApiOperation(value = "user에 맞는 childComment 등록함")
    @ResponseBody
    public String saveChildCommentByNoneUser(@PathVariable Long boardId,
                                             @RequestBody NoneUserChildCommentDto noneUserChildCommentDto,
                                             HttpServletRequest request) throws Exception {

        System.out.println("content = " + noneUserChildCommentDto.getContent());
        List<UserAuthority> userAuthorities = new ArrayList<>();
        userAuthorities.add(new UserAuthority(new Authority(RoleStatus.ROLE_ANONYMOUS.name())));
        CookieManager cookieManager = new CookieManager();
        NoneMember user = NoneMember.builder()
                .userId(cookieManager.getUUidCookie(request))
                .nickname(noneUserChildCommentDto.getNickname())
                .userId(cookieManager.getUUidCookie(request))
                .password(noneUserChildCommentDto.getPassword())
                // .ip(requestIpApi.getClientIpAddr(request))
                .ip(request.getRemoteAddr())
                .userAuthorities(userAuthorities)
                .build();
        userService.userAndUserAuthoritySave(user);
        Board board = boardService.findAllByImageId(boardId);
        // ParentComment parentComment = commentService.getParentComment(userChildCommentDto.getParentId());
        System.out.println("CommentController - saveChildCommentByNoneUser()noneUserChildCommentDto.getParentId() = " + noneUserChildCommentDto.getParentId());
        UserAndBoardAndParentCommentDto userAndBoardAndParentCommentDto = new UserAndBoardAndParentCommentDto(board,user,new ObjectId(noneUserChildCommentDto.getParentId()));
        System.out.println("CommentController - saveChildCommentByNoneUser().userAndBoardAndParentCommentDto = " + userAndBoardAndParentCommentDto);
        commentService.saveOnlyChildComment(noneUserChildCommentDto.getContent(),userAndBoardAndParentCommentDto);
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
    public List<AllCommentReadDataDto> getBoard(@PathVariable Long boardId) throws Exception {
        CommentReadViewDto commentDto = new CommentReadViewDto();
        commentDto.setBoardId(boardId);
        List<ParentComment> parentComments = commentService.readTenParentComment(commentDto);
        List<AllCommentReadDataDto> allCommentReadDataDtos = new ArrayList<>();

        System.out.println("CommentController.getBoard() - parentComments.size() = " + parentComments.size());
        if(parentComments!= null ) {
            parentComments.forEach(parentComment -> {
                List<CommentReadDataDto> childCommentReadDataDtos = new ArrayList<>();
                    if(parentComment.getChildComments()!=null) {
                        System.out.println("CommentController.getBoard() - parentComments.getChildComments() = " + parentComment.getChildComments().size());
                        childCommentReadDataDtos = setChildCommentReadDataDtos(parentComment.getChildComments());
                    }


                    allCommentReadDataDtos.add(AllCommentReadDataDto.builder()
                            .objectId(parentComment.getId().toString())
                            .nickname(parentComment.getParentCommentNickname())
                            .userId(parentComment.getParentCommentUserId())
                            .content(parentComment.getParentCommentContent())
                            .childCommentReadDataDtos(childCommentReadDataDtos)
                            .build());
                    }
            );
        }

        return allCommentReadDataDtos;
    }
    public List<CommentReadDataDto> setChildCommentReadDataDtos(List<ChildComment> childComments) {
        List<CommentReadDataDto> childCommentReadDataDtos = new ArrayList<>();
        if(childComments.size()>0) {
            for (ChildComment childComment : childComments) {
                childCommentReadDataDtos.add(new CommentReadDataDto(childComment));
            }
        }
        return childCommentReadDataDtos;

    }

}


