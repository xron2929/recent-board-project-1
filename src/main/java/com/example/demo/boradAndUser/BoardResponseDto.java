package com.example.demo.boradAndUser;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardResponseDto {
    private Long id;
    private String title;
    private String boardWriterName;

    private String contents;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public void setId(Long id) {
        this.id = id;
    }
    public BoardResponseDto(Long id, String title,
                            String boardWriterName, String contents,
                            LocalDateTime createdDate, LocalDateTime lastModifiedDate
                            ) {
        this.id = id;
        this.title = title;
        this.boardWriterName = boardWriterName;
        this.contents = contents;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }


}

