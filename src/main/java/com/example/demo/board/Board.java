package com.example.demo.board;


import com.example.demo.baseTime.BaseTimeEntity;
import com.example.demo.comment.ParentComment;
import com.example.demo.image.ImageStore;
import com.example.demo.user.defaultuser.DefaultMember;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@BatchSize(size = 100)
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    // @OneToMany(mappedBy = "board",orphanRemoval = true,cascade = CascadeType.REMOVE)
    // private List<ParentComment> comments = new ArrayList<>();
    // 게시글 하나가 덧글을 가져오지만 덧글은 게시글을 가져오지 않음
    @OneToMany(mappedBy = "board",orphanRemoval = true,cascade = CascadeType.REMOVE)
    private List<ImageStore> images = new ArrayList<>();
    @NonNull
    private boolean isSecret;
    public Board(String title, String content, DefaultMember member,boolean isSecret) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.isSecret = isSecret;

    }


    public Board(Long id,String title,String content,boolean isSecret) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isSecret = isSecret;
    }

    public Board(Long id, String title, String content, DefaultMember member,boolean isSecret) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.member = member;
        this.isSecret = isSecret;
    }

    public Board(DefaultMember member, String content,String title,boolean isSecret) {
        this.content = content;
        this.member = member;
        this.isSecret = isSecret;
        this.title = title;
    }

    public Board(@NonNull boolean isSecret) {
        this.isSecret = isSecret;
    }
}
