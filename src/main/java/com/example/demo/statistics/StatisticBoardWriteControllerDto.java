package com.example.demo.statistics;

import lombok.Getter;

import java.math.BigInteger;

@Getter
public class StatisticBoardWriteControllerDto {
    private String authorityName;
    private BigInteger boardCount;
    private String localDateTime;

    public StatisticBoardWriteControllerDto(String authorityName, BigInteger boardCount, String localDateTime) {
        this.authorityName = authorityName;
        this.boardCount = boardCount;
        this.localDateTime = localDateTime;
    }
}
