package com.example.demo.alarm;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Getter
public class AlarmSaveDto {
    @Builder
    public AlarmSaveDto(@NonNull Long boardId, @NonNull String summaryCommentContent,
                        @NonNull String commentWriter, @NonNull Boolean isVisited) {
        this.boardId = boardId;
        this.summaryCommentContent = summaryCommentContent;
        this.commentWriter = commentWriter;
        this.isVisited = isVisited;
    }


    @NonNull private Long boardId;
    @NonNull private String summaryCommentContent;
    @NonNull private String commentWriter;
    @NonNull private Boolean isVisited;
}
