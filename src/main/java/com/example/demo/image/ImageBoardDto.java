package com.example.demo.image;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ImageBoardDto {

    @Getter
    private Long boardId;
    @Getter private String filePath;
    public ImageBoardDto( Long boardId,String filePath) {
        this.boardId = boardId;
        this.filePath = filePath;
    }
}

