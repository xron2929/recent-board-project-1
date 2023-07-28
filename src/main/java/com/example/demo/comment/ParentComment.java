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
public class ParentComment extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Long cDepth;
    @NonNull
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id")
    private Board board;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private DefaultMember member;

    public ParentComment(Long cDepth, String content, Board board, DefaultMember member) {
        this.cDepth = cDepth;
        this.content = content;
        this.board = board;
        this.member = member;
    }

    public ParentComment( String content, Board board, DefaultMember member) {
        this.content = content;
        this.board = board;
        this.member = member;
    }

}

