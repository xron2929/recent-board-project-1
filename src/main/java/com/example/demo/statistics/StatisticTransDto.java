package com.example.demo.statistics;

import lombok.Getter;
import lombok.Setter;

@Getter
public class StatisticTransDto {
    private Long unSelectedCount;
    private Long maleCount;
    private Long femaleCount;
    private Long totalCount;
    public StatisticTransDto(Long unSelectedCount, Long maleCount, Long femaleCount, Long totalCount) {
        this.unSelectedCount = unSelectedCount;
        this.maleCount = maleCount;
        this.femaleCount = femaleCount;
        this.totalCount = totalCount;
    }

    @Setter private String femalePercent;
    @Setter private String unSelectedPercent;
    @Setter private String malePercent;

}
