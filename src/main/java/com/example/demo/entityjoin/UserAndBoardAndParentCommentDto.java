package com.example.demo.entityjoin;

import com.example.demo.board.Board;
import com.example.demo.comment.ParentComment;
import com.example.demo.user.defaultuser.DefaultMember;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
public class UserAndBoardAndParentCommentDto {
    private Board board;
    private DefaultMember defaultMember;
    private ObjectId parentCommentId;
    public UserAndBoardAndParentCommentDto(Board board, DefaultMember defaultMember,ObjectId parentCommentId) {
        this.board = board;
        this.defaultMember = defaultMember;
        this.parentCommentId = parentCommentId;
    }


}