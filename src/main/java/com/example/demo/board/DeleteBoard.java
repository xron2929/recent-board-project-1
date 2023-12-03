package com.example.demo.board;


import com.example.demo.baseTime.BaseTimeEntity;
import com.example.demo.board.image.ImageStore;
import com.example.demo.user.defaultuser.DefaultMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteBoard extends BaseTimeEntity {

    @Id
    @Column(name="board_id")
    @Setter
    private Long id;

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    @Column(columnDefinition = "TEXT")
    @Setter private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private DefaultMember member;
    // 저자

    // 게시글 하나가 덧글을 가져오지만 덧글은 게시글을 가져오지 않음
    @OneToMany(mappedBy = "board",orphanRemoval = true,cascade = CascadeType.ALL)
    private List<ImageStore> images = new ArrayList<>();

    public DeleteBoard(String title, DefaultMember member) {
        this.title=title;
        this.member=member;
    }
    public DeleteBoard(String title, String content, DefaultMember member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }


    public DeleteBoard(Long id,String title,String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
    public DeleteBoard(Long id, String title, String content, DefaultMember member) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public DeleteBoard(DefaultMember member, String content) {
        this.content = content;
        this.member = member;
    }

}
