package com.example.demo.entityjoin;


import com.example.demo.board.Board;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserBoardFindDataDto {
// like,disLike를 가져옴
    private Long id;
    private String username;
    private String password;
    private String title;
    private String content;
    private String likeCount;
    private String disLikeCount;

    @Builder
    public UserBoardFindDataDto(String title, String content, String username, String password,String likeCount,String disLikeCount) {
        this.title = title;
        this.content = content;
        this.username = username;
        this.password = password;
        this.likeCount = likeCount;
        this.disLikeCount = disLikeCount;
    }

    public UserBoardFindDataDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.username = board.getMember().getUserId();
        this.password = board.getMember().getPassword();
    }
}


