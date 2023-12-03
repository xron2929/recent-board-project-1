package com.example.demo.alarm;

import com.example.demo.util.BoardQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoneUserAlarmService {
    @Autowired
    NoneUserAlarmRepository noneUserAlarmRepository;
    @Autowired
    NoneUserAlarmDslRepository noneUserAlarmDslRepository;
    @Autowired AlarmRepository alarmRepository;
    public List<Alarm> getAlarms(Pageable pageable, String userName) {
        BoardQueryDto boardQueryDto = new BoardQueryDto();
        return findNoneUserAlarmData(pageable,userName);
    }


    public NoneUserAlarm changeAlarm(Long alarmId) {
        NoneUserAlarm noneUserAlarm = noneUserAlarmRepository.findById(alarmId).orElse(null);
        noneUserAlarm.setIsVisited(true);
        return noneUserAlarmRepository.save(noneUserAlarm);
    }

    public void saveAlarm(NoneUserAlarm noneUserAlarm) {
        noneUserAlarmRepository.save(noneUserAlarm);
    }

    // offset을 나중에 1,2,3,4,5 컨트롤러 용으로 따로 추가해야됨..
    public List<Alarm> findNoneUserAlarmData(Pageable pageable, String userName) {

        List<Alarm> byIdIsBetween = alarmRepository.findByBoardWriterId(userName, pageable).getContent();
        System.out.println("noneUser - byIdIsBetween = " + byIdIsBetween.size());
        return byIdIsBetween;
    }

}

