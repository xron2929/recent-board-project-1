package com.example.demo.alarm;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlarmService {
    @Autowired
    AlarmRepository alarmRepository;
    @Autowired
    AlarmDslRepository alarmDslRepository;
    @Autowired
    AdditionalInformationRedisTemplate redisTemplate;
    public Alarm changeAlarm(Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElse(null);
        alarm.setIsVisited(true);
        return alarmRepository.save(alarm);
    }

    public void saveAlarm(Alarm alarm) {
        Alarm saveAlarm = alarmRepository.save(alarm);
        try {
            redisTemplate.setAlarm(alarm.getBoardWriterId(),saveAlarm);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public Map<String, List<Alarm>> findALLAlarms() {
        List<Alarm> all = alarmRepository.findAll();
        Map<String,List<Alarm>> alarms = new HashMap<>();
        all.forEach(alarm -> {
            List<Alarm> list = alarms.get(alarm.getBoardWriterId());
            if (list == null) {
                list = new ArrayList<>();
                alarms.put(alarm.getBoardWriterId(), list);
            }
            list.add(alarm);
        });
        return alarms;
    }
    public Long getAlarmSize(String userId) {
        return redisTemplate.getSize(userId);
    }
    public List<Alarm> findAlarmData(Pageable pageable, String boardWriterId) {
        System.out.println("pageable.getPageSize() = " + pageable.getPageSize());
        System.out.println("pageable.getPageSize() = " + pageable.getPageNumber());
        BoardCalculator boardCalculator = new BoardCalculator();
        BoardQueryDto boardQueryDto = new
                BoardQueryDto(pageable.getPageNumber(),pageable.getPageSize());
        BoardQueryDto calculate = boardCalculator.calculate(boardQueryDto);
        Long startBoardQuantity = calculate.getStartBoardQuantity();
        System.out.println("startBoardQuantity = " + startBoardQuantity);
        System.out.println("boardWriterId = " + boardWriterId);
        System.out.println("pageable.getPageNumber()*pageable.getPageSize() = " + pageable.getPageNumber()*pageable.getPageSize());
        List<Alarm> alarms = redisTemplate.getAlarms(pageable.getPageSize() * pageable.getPageNumber(), ((pageable.getPageNumber()+1) * pageable.getPageSize()-1), boardWriterId);
        return alarms;
    }

}


