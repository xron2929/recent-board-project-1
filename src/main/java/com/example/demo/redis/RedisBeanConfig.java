package com.example.demo.redis;

import com.example.demo.alarm.AdditionalInformationRedisTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisBeanConfig {

    public RedisBeanConfig(@Qualifier("dtoTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    private RedisTemplate<String,Object> redisTemplate;
    @Bean
    public AdditionalInformationRedisTemplate additionalInformationRedisTemplate() {
        return new AdditionalInformationRedisTemplate(redisTemplate);
    }
}
