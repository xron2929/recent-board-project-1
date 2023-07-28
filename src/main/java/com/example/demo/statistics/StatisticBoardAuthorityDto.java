package com.example.demo.statistics;

import lombok.Getter;
import lombok.Setter;

@Getter
public class StatisticBoardAuthorityDto {
    private Long oauthUserCount;
    private Long siteUserCount;
    private Long noneUserCount;
    private Long totalCount;

    public StatisticBoardAuthorityDto(Long oauthUserCount, Long siteUserCount, Long noneUserCount, Long totalCount) {
        this.oauthUserCount = oauthUserCount;
        this.siteUserCount = siteUserCount;
        this.noneUserCount = noneUserCount;
        this.totalCount = totalCount;
    }

    @Setter
    private String oauthUserPercent;
    @Setter private String siteUserPercent;
    @Setter private String noneUserPercent;
}
