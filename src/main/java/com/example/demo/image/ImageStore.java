package com.example.demo.image;


import com.example.demo.board.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class ImageStore {
    @Id
    private String filePath;
    @CreatedDate
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id")
    private Board board;


    @Override
    public String toString() {
        return "ImageStore{" +
                "filePath='" + filePath + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
