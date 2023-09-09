package com.example.demo.alarm;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Getter
public class NoneUserAlarmSaveDto {
    @Builder
    public NoneUserAlarmSaveDto(@NonNull Long boardId, @NonNull String summaryCommentContent,
                                @NonNull String userName,
                                @NonNull String boardWriterId, @NonNull Boolean isVisited) {
        this.boardId = boardId;
        this.summaryCommentContent = summaryCommentContent;
        this.boardWriterId = boardWriterId;
        this.userName = userName;
        this.isVisited = isVisited;
    }
    @NonNull private String boardWriterId;
    @NonNull private Long boardId;
    @NonNull private String summaryCommentContent;
    @NonNull private Boolean isVisited;
    @NonNull private String userName;
}


