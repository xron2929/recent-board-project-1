package com.example.demo.entityjoin;


import com.example.demo.board.Board;
import com.example.demo.user.defaultuser.DefaultMember;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentSaveDataDto {
    private Board board;
    private DefaultMember member;
    private String content;

}

