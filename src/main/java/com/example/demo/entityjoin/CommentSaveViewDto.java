package com.example.demo.entityjoin;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CommentSaveViewDto {
    @NonNull
    private String userId;
    @NonNull @Setter private Long boardId;
    @NonNull private String content;
    @NonNull private String password;


}
