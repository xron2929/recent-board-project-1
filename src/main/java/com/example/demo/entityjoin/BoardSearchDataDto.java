package com.example.demo.entityjoin;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BoardSearchDataDto {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdDate;
}
