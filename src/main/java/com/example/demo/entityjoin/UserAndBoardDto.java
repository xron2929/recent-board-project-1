package com.example.demo.entityjoin;

import com.example.demo.board.Board;
import com.example.demo.user.defaultuser.DefaultMember;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserAndBoardDto {
    private Board board;
    private DefaultMember defaultMember;

    public UserAndBoardDto(Board board, DefaultMember defaultMember) {
        this.board = board;
        this.defaultMember = defaultMember;
    }
}
