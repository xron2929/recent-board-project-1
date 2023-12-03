package com.example.demo.comment;

import com.example.demo.comment.ChildComment;
import com.example.demo.entityjoin.CommentReadDataDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@Builder
public class AllCommentReadDataDto {
    private String objectId;
    private String userId;
    private String content;
    private String nickname;
     private List<CommentReadDataDto>childCommentReadDataDtos;

    public AllCommentReadDataDto(String objectId,String userId, String content,String nickname,
                                 List<CommentReadDataDto>childCommentReadDataDtos) {
        this.objectId = objectId;
        this.userId = userId;
        this.content = content;
        this.nickname = nickname;
        this.childCommentReadDataDtos = childCommentReadDataDtos;
    }



}

