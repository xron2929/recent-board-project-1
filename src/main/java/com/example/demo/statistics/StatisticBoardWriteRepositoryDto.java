package com.example.demo.statistics;

import lombok.Getter;

import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
public class StatisticBoardWriteRepositoryDto {
    private String authorityName;
    private BigInteger boardCount;
    private Timestamp localDateTime;

    public StatisticBoardWriteRepositoryDto(String authorityName, BigInteger boardCount, Timestamp localDateTime) {
        this.authorityName = authorityName;
        this.boardCount = boardCount;
        this.localDateTime = localDateTime;
    }
}
