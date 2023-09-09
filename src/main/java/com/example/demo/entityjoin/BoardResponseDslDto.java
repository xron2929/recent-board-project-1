package com.example.demo.entityjoin;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDslDto {
    private Long id;
    private String title;
    private String boardWriterName;
    private String contents;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String likeCount;
    private String disLikeCount;

    public void setId(Long id) {
        this.id = id;
    }
    @Builder
    public BoardResponseDslDto(@NonNull Long id, @NonNull String title,
                               @NonNull String boardWriterName, @NonNull String contents,
                               @NonNull LocalDateTime createdDate, @NonNull LocalDateTime lastModifiedDate,
                               @NonNull String likeCount,@NonNull String disLikeCount) {
        this.id = id;
        this.title = title;
        this.boardWriterName = boardWriterName;
        this.contents = contents;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.likeCount = likeCount;
        this.disLikeCount = disLikeCount;
    }
}
