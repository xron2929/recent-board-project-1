package com.example.demo.alarm;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlarmDto {
    public AlarmDto(Long id, String title, Long boardId,
                    String summaryCommentContent, String commentWriter,
                    Boolean isVisited, String boardWriterId) {
        this.id = id;
        this.title = title;
        this.boardId = boardId;
        this.summaryCommentContent = summaryCommentContent;
        this.commentWriter = commentWriter;
        this.isVisited = isVisited;
        this.boardWriterId = boardWriterId;
    }

    private Long id;
    private String title;
    private Long boardId;
    private String summaryCommentContent;
    private String commentWriter;
    private Boolean isVisited;
    private String boardWriterId;

}

