package com.example.demo.alarm;


import com.example.demo.user.oauthuser.OauthUserFirstJoinDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AdditionalInformationRedisTemplate {
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    ObjectMapper objectMapper;

    public AdditionalInformationRedisTemplate(
            @Qualifier("dtoTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setAllAlarms(String userId, List<Alarm> alarms) {
        List<String> objectData = new ArrayList<>();
        alarms.forEach(alarm -> {
            try {
                objectData.add(objectMapper.writeValueAsString(alarm));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        redisTemplate.opsForList().rightPushAll(userId, objectData);
    }

    public void setAlarm(String uuid, Alarm alarm) throws JsonProcessingException {
        String data = objectMapper.writeValueAsString(alarm);
        redisTemplate.opsForList().leftPush(uuid, data);
    }

    public List<Alarm> getAlarms(long startId, long endId, String boardWriterId) {
        List<Object> objects = redisTemplate.opsForList().range(boardWriterId, startId, endId);
        List<Alarm> alarms = new ArrayList<>();
        objects.forEach(object -> {
            try {
                System.out.println("(String) object = " + (String) object);
                Alarm alarm = objectMapper.readValue((String) object, Alarm.class);
                System.out.println("alarm.getBoardWriterId() = " + alarm.getBoardWriterId());
                System.out.println("alarm.getTitle() = " + alarm.getTitle());
                alarms.add(alarm);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return alarms;
    }

    public List<Object> getAllAlarms(String key) {
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        long size = ops.size(key);
        return ops.range(key, 0, size - 1);
    }

    public Long getSize(String key) {
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        long size = ops.size(key);
        return size;
    }

    public void setOauthUserFirstJoinDto(String userId, OauthUserFirstJoinDto firstJoinDto) throws JsonProcessingException {
        String firstJoinDtoJson = objectMapper.writeValueAsString(firstJoinDto);
        redisTemplate.opsForValue().set("OauthUserFirstJoinDto: "+userId, firstJoinDtoJson, 60, TimeUnit.MINUTES);
    }

    public OauthUserFirstJoinDto getOauthUserFirstJoinDto(String userId) throws JsonProcessingException, IllegalArgumentException {
        System.out.println("userId = " + userId);
        OauthUserFirstJoinDto oauthUserFirstJoinDto = objectMapper.readValue((String) redisTemplate.opsForValue().get("OauthUserFirstJoinDto: "+userId), OauthUserFirstJoinDto.class);
        System.out.println("AdditionalRestTemplate - getOauthUserFirstJoinDto = " + oauthUserFirstJoinDto);
        return oauthUserFirstJoinDto;
    }

    public void setOauthUserFirstJoinUrl(String userId, String url) throws JsonProcessingException {
        redisTemplate.opsForValue().set("url: " + userId, url);
    }
    public String getOauthUserFirstJoinUrl(String userId) throws JsonProcessingException {
        String url = (String) redisTemplate.opsForValue().get("url: " + userId);
        System.out.println("AdditionalRedisTemplate - getOauthUserFirstJoinUrl = " + url);
        return url;
    }

}

