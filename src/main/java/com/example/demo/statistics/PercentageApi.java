package com.example.demo.statistics;

import org.springframework.stereotype.Component;

@Component
public class PercentageApi {
    private static Long SCALING_POINT = 100L;
    private static Long PERCENTAGE_UNIT = 100L;
    private Long decimalPart;
    private Long integerPart;
    public String getPercentage(Long specificDataCount,Long totalCount) {
        Long temp = (((specificDataCount * PERCENTAGE_UNIT) * SCALING_POINT) / totalCount);
        integerPart = temp / SCALING_POINT;
        decimalPart = temp % SCALING_POINT;
        String resultPercentageNumber = integerPart+"."+decimalPart+"%";
        return resultPercentageNumber;
    }

}
