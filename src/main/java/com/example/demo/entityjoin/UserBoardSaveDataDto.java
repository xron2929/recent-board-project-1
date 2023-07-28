package com.example.demo.entityjoin;


import com.example.demo.board.Board;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class UserBoardSaveDataDto {

    private String userId;

    public UserBoardSaveDataDto(String userId) {
        this.userId = userId;
    }
}

