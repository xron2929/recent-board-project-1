package com.example.demo.statistics;

import com.example.demo.role.RoleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticService {
    @Autowired
    StatisticRepository statisticRepository;
    @Autowired PercentageApi percentageApi;
    public StatisticTransDto getTransData() {
        StatisticTransDto transOauthDto = statisticRepository.getTransOauthData();
        String malePercent = percentageApi.getPercentage(transOauthDto.getMaleCount(),
                transOauthDto.getTotalCount());
        String femalePercent = percentageApi.getPercentage(transOauthDto.getFemaleCount(),
                transOauthDto.getTotalCount());
        String unSelectedPercent = percentageApi.getPercentage(transOauthDto.getUnSelectedCount(),
                transOauthDto.getTotalCount());
        transOauthDto.setMalePercent(malePercent);
        transOauthDto.setFemalePercent(femalePercent);
        transOauthDto.setUnSelectedPercent(unSelectedPercent);
        return transOauthDto;
    }
    public StatisticBoardAuthorityDto getBoardAuthorityData() {
        StatisticBoardAuthorityDto boardAuthorityDto = statisticRepository.getAuthorityData();
        String oauthUserPercent = percentageApi.getPercentage(boardAuthorityDto.getOauthUserCount(),
                boardAuthorityDto.getTotalCount());
        String siteUserPercent = percentageApi.getPercentage(boardAuthorityDto.getSiteUserCount(),
                boardAuthorityDto.getTotalCount());
        String unSelectedPercent = percentageApi.getPercentage(boardAuthorityDto.getNoneUserCount(),
                boardAuthorityDto.getTotalCount());
        boardAuthorityDto.setOauthUserPercent(oauthUserPercent);
        boardAuthorityDto.setSiteUserPercent(siteUserPercent);
        boardAuthorityDto.setNoneUserPercent(unSelectedPercent);
        return boardAuthorityDto;
    }
    public List<StatisticBoardWriteControllerDto> getBoardWriteDataDtos() throws TimerException {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusMinutes(4);
        System.out.println("startTime = " + startTime);
        System.out.println("endTime = " + endTime);
        List<StatisticBoardWriteControllerDto> statisticBoardWriteControllerDtos = new ArrayList<>();
        List<StatisticBoardWriteRepositoryDto> statisticBoardWriteRepositoryDtos = statisticRepository.getPeriodicBoardWritingCount(startTime, endTime);
        for (StatisticBoardWriteRepositoryDto staticBoardWriteRepositoryDto:statisticBoardWriteRepositoryDtos){
            LocalDateTime localDateTime = staticBoardWriteRepositoryDto.getLocalDateTime().toLocalDateTime();
            long secondsDiff = ChronoUnit.SECONDS.between(localDateTime, endTime);
            String timeDifference = getTimeDifference(secondsDiff);

            statisticBoardWriteControllerDtos.add(new StatisticBoardWriteControllerDto
                    (convertRoleToUsername(staticBoardWriteRepositoryDto.getAuthorityName()),staticBoardWriteRepositoryDto.getBoardCount(),timeDifference));
        }
        return statisticBoardWriteControllerDtos;
    }

    private String getTimeDifference(Long secondsDiff) throws TimerException {
        String timeStatus;
        if (secondsDiff <= 60) {
            // 59초
            timeStatus = TimeStatus.ONE_MIN_TO_CURRENT.getTimeMessage();
        } else if(secondsDiff > 60 && secondsDiff <= 120) {
            timeStatus = TimeStatus.SECOND_MIN_TO_ONE_MIN.getTimeMessage();
        } else if(secondsDiff > 120 && secondsDiff <= 180) {
            timeStatus = TimeStatus.THIRD_MIN_TO_SECOND_MIN.getTimeMessage();
        } else if(secondsDiff > 180 && secondsDiff <= 240) {
            timeStatus = TimeStatus.FOURTH_MIN_TO_THIRD_MIN.getTimeMessage();
        }  else {
            throw new TimerException("정해진 예상 시간 데이터와는 다름");
        }
        return timeStatus;
    }
    private String convertRoleToUsername(String role) {
        if(RoleStatus.ROLE_OAUTH_USER.name().equals(role)) {
            return "oauthUser";
        }
        if(RoleStatus.ROLE_SITE_USER.name().equals(role)) {
            return "siteUser";
        }
        if(RoleStatus.ROLE_ANONYMOUS.name().equals(role)) {
            return "noneUser";
        }
        throw new ConvertNameException("role 권한에 없는 내역이 포함되어 있음");
    }
}
