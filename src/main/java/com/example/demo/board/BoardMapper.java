package com.example.demo.board;


import com.example.demo.user.MemberBoardQueryDTO;
import com.example.demo.user.MemberBoardQueryDTOInterface;
import org.springframework.stereotype.Component;

@Component
public class BoardMapper {

    public DeleteBoard changeDeleteBoard(Board board) {
        DeleteBoard deleteBoard = new DeleteBoard(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getMember(),
                board.getComments(),
                board.getImages()
        );
        deleteBoard.setCreatedDate(board.getCreatedDate());
        return deleteBoard;
    }

}

