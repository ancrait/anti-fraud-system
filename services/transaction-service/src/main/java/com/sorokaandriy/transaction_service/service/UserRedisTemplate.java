package com.sorokaandriy.transaction_service.service;

import com.sorokaandriy.transaction_service.model.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParseException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j
public class UserRedisTemplate {

    private final StringRedisTemplate redisTemplate;

    public UserRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isUserBlocked(String userId) {
        Boolean blocked = redisTemplate.hasKey(userId);
        return blocked != null && blocked;
    }
}
