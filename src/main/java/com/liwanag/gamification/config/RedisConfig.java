package com.liwanag.gamification.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

//        ObjectMapper redisMapper = objectMapper.copy().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        GenericJackson2JsonRedisSerializer json = new GenericJackson2JsonRedisSerializer(redisMapper);
        RedisSerializer<Object> json = RedisSerializer.json();

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(json);
        return template;
    }

    @Bean("strRedisTemplate")
    public RedisTemplate<String, String> strRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}
