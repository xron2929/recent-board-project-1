package com.example.demo.comment;


import com.example.demo.baseTime.BaseTimeEntity;
import com.example.demo.board.Board;
import com.example.demo.user.defaultuser.DefaultMember;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(indexes = @Index(name = "i_parent_comment",columnList = "board_id,id"))
public class ParentComment extends DefaultComment {

    public ParentComment(String content, Board board, DefaultMember member) {
        super(content, board, member);
        this.content = content;
        this.board = board;
        this.member = member;
    }
}

