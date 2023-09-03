package com.example.demo.statistics;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class StatisticController {
    @Autowired
    StatisticService statisticService;

    @ApiOperation("통계 뷰 반환")
    @GetMapping("/static")
    public String getView() {
        return "static/statics";
    }
    @ApiOperation("성별 데이터 반환")
    @GetMapping("/static/trans/data")
    @ResponseBody
    public StatisticTransDto getTransData() {
        return statisticService.getTransData();
    }
    @ApiOperation("board 작성자 유저 권한 비율")
    @GetMapping("/static/board/authority/data")
    @ResponseBody
    public StatisticBoardAuthorityDto getAuthorityData() {
        // 모든 Board를 기준으로 Oauth 사용자 비율 noneUser, SiteUser(원 그래프)
        return statisticService.getBoardAuthorityData();
    }
    @ApiOperation("분당 board 글쓰기 통계")
    @GetMapping("/static/write/data")
    @ResponseBody
    public List<StatisticBoardWriteControllerDto> getBoardWriteData() throws TimerException {
        // 4분전 3분전 2분전 1분전 현재 기준으로 각각 그 사이 기간의 꺾은 선  Board 쓴 시간의 그래프를 작성ㅊ
        return statisticService.getBoardWriteDataDtos();
    }



}
