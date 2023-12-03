package com.example.demo.alarm;

import com.example.demo.util.BoardCalculator;
import com.example.demo.util.BoardQueryDto;
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
        List<Alarm> alarms = redisTemplate.getAlarms(pageable.getPageSize() * pageable.getPageNumber(), ((pageable.getPageNumber()+1) * pageable.getPageSize()-1), boardWriterId);
        return alarms;
    }

}


