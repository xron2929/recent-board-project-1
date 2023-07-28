package com.example.demo.alarm;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Entity
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public class Alarm {

    @Id
    @Column(name = "id",unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="boardWriterId")
    private String boardWriterId;

    public Alarm(@NonNull String title, @NonNull Long boardId,
                 @NonNull String summaryCommentContent, @NonNull String commentWriter,
                 @NonNull Boolean isVisited) {
        this.title = title;
        this.boardId = boardId;
        this.summaryCommentContent = summaryCommentContent;
        this.commentWriter = commentWriter;
        this.isVisited = isVisited;
    }

    @NonNull
    private String title;
    @NonNull private Long boardId;
    @NonNull private String summaryCommentContent;
    @NonNull private String commentWriter;
    @NonNull private Boolean isVisited;

}

