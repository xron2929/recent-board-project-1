package com.example.demo.entityjoin;

import lombok.*;
import org.bson.types.ObjectId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ChildCommentSaveViewDto {
    @NonNull
    private String nickname;
    private String userId;
    @NonNull @Setter private Long boardId;
    @NonNull private String content;
    @NonNull private String password;
    @NonNull private String parentCommentId;
}
