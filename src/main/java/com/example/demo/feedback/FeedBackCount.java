package com.example.demo.feedback;


import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
@Getter
public class FeedBackCount {
    private String likeCount;
    private String disLikeCount;
    @Builder
    public FeedBackCount(@NonNull String likeCount, @NonNull String disLikeCount) {
        this.likeCount = likeCount;
        this.disLikeCount = disLikeCount;
    }
}
