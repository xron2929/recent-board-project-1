package com.example.demo.comment;


import com.example.demo.board.Board;
import com.example.demo.board.BoardService;
import com.example.demo.entityjoin.ChildCommentSaveViewDto;
import com.example.demo.entityjoin.ParentCommentSaveViewDto;
import com.example.demo.entityjoin.UserAndBoardAndParentCommentDto;
import com.example.demo.entityjoin.UserAndBoardDto;
import com.example.demo.user.UserService;
import com.example.demo.user.defaultuser.DefaultMember;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommentMapper {
    // user 저장하고
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    BoardService boardService;
    // mapper vs 서비스
    public ParentComment saveMemberAndComment(ParentCommentSaveViewDto parentCommentSaveViewDto, String uuidCookie) throws Exception {
        // 여기서 ParentComment 데이터 등록하기
        // 그거 끝나면 ChildComment도 등록하고 글 수정,삭제,업데이트 다 올려야됨
        // 그거 끝나면 다 대규모 트래픽 고려해서도 n개에 대한 처리 짜야될꺼고...
        // 리액트도 올려야됨 그래서 되게 바쁨 ㅇㅇ..
        DefaultMember user = DefaultMember
                .builder()
                .userId(uuidCookie)
                .nickname(parentCommentSaveViewDto.getNickname())
                .password(parentCommentSaveViewDto.getPassword())
                .build();
        DefaultMember saveUser = userService.saveUser(user);
        // System.out.println("saveUser.getPassword() = " + saveUser.getPassword());
        Board board = boardService.findAllByImageId(parentCommentSaveViewDto.getBoardId());
        if(board == null) {
            throw new RuntimeException();
        }
        System.out.println("board.getTitle() = " + board.getTitle());
        String content = parentCommentSaveViewDto.getContent();

        ParentComment parentComment = new ParentComment(content,user.getUserId(),user.getPassword(),board.getId());
        return parentComment;
    }
    public ChildComment saveMemberAndComment(ChildCommentSaveViewDto childCommentSaveViewDto, String uuidCookie) throws Exception {
        // 여기서 ParentComment 데이터 등록하기
        // 그거 끝나면 ChildComment도 등록하고 글 수정,삭제,업데이트 다 올려야됨
        // 그거 끝나면 다 대규모 트래픽 고려해서도 n개에 대한 처리 짜야될꺼고...
        // 리액트도 올려야됨 그래서 되게 바쁨 ㅇㅇ..
        DefaultMember user = DefaultMember
                .builder()
                .userId(uuidCookie)
                .nickname(childCommentSaveViewDto.getNickname())
                .password(childCommentSaveViewDto.getPassword())
                .build();
        DefaultMember saveUser = userService.saveUser(user);
        // System.out.println("saveUser.getPassword() = " + saveUser.getPassword());
        Board board = boardService.findAllByImageId(childCommentSaveViewDto.getBoardId());
        if(board == null) {
            throw new RuntimeException();
        }
        System.out.println("board.getTitle() = " + board.getTitle());
        String content = childCommentSaveViewDto.getContent();

        ChildComment childComment = ChildComment.builder()
                .userId(saveUser.getUserId())
                .authorName(saveUser.getNickname())
                .parentCommentId(new ObjectId(childCommentSaveViewDto.getParentCommentId())).createdDate(LocalDateTime.now()).lastModifiedDate(LocalDateTime.now()).build();
        return childComment;
    }
    public ParentComment saveOnlyCommendDto(String content, UserAndBoardDto userAndBoardDto) throws Exception {
        ParentComment parentComment = new ParentComment(content,userAndBoardDto.getDefaultMember().getUserId(),userAndBoardDto.getDefaultMember().getNickname(),userAndBoardDto.getBoard().getId());
        return parentComment;
    }

    public ChildComment saveOnlyCommendDto(String content, UserAndBoardAndParentCommentDto userAndBoardAndParentCommentDto) throws Exception {
        ChildComment childComment = new ChildComment(content,
                userAndBoardAndParentCommentDto.getDefaultMember().getUserId(),
                userAndBoardAndParentCommentDto.getDefaultMember().getNickname(),
                userAndBoardAndParentCommentDto.getParentCommentId(),
                LocalDateTime.now(),LocalDateTime.now());

        return childComment;
    }
    public List<ParentComment> saveCommendDtos(List<ParentCommentSaveViewDto> commentViewDtos) throws Exception {
        List<ParentComment> parentComments = new ArrayList<>();
        for (ParentCommentSaveViewDto commentViewDto:commentViewDtos){
            DefaultMember user = DefaultMember.builder()
                    .nickname(commentViewDto.getNickname())
                    .userId(commentViewDto.getUserId())
                    .password(commentViewDto.getPassword())
                    .build();
            DefaultMember saveUser = userService.saveUser(user);
            System.out.println("saveUser.getPassword() = " + saveUser.getPassword());
            Board board = boardService.findAllByImageId(commentViewDto.getBoardId());
            if(board == null) {
                throw new RuntimeException();
            }
            System.out.println("board.getContent() = " + board.getContent());
            System.out.println("board.getTitle() = " + board.getTitle());
            String content = commentViewDto.getContent();
            ParentComment parentComment = new ParentComment(content,user.getUserId(),user.getNickname(), board.getId());
            parentComments.add(parentComment);
        }

        //return new CommentInsertDataDto(board,saveUser,content);

        return parentComments;

    }

}

