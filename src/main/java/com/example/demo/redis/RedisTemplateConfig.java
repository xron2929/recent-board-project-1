package com.example.demo.redis;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisTemplateConfig {
    RedisConnectionFactory redisConnectionFactory;

    public RedisTemplateConfig(@Qualifier("defaultRedisTemplate") RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    // 이메일 서비스 value도 String 매핑할예정이라 딱히..
    // 키-value
    @Bean
    @Qualifier("crudTemplate")
    public RedisTemplate<Object, Object> template() {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        // template.setValueSerializer(new Jackson2JsonRedisSerializer<Pencil>(Pencil.class));
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }
    // 안의 내부 타입 저장하려고 한듯
    @Bean
    @Qualifier("dtoTemplate")
    public RedisTemplate<String, Object> dtoTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        // template.setValueSerializer(new Jackson2JsonRedisSerializer<Pencil>(Pencil.class));
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }
}

