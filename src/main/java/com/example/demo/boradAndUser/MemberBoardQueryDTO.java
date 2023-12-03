package com.example.demo.boradAndUser;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberBoardQueryDTO {
    private Long boardId;
    private String title;
    private String contents;
    private String userName;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Long boardCount;

    public MemberBoardQueryDTO(Long boardId, String title, String contents, String userName,LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.boardId = boardId;
        this.title = title;
        this.contents = contents;
        this.userName = userName;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }
    public MemberBoardQueryDTO(String title, String contents, String userName) {
        this.title = title;
        this.contents = contents;
        this.userName = userName;
    }


}
