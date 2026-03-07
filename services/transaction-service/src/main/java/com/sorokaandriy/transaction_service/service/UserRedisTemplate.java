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
    private final static String BLACKLIST_KEY = "blacklisted_users";

    public UserRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isUserBlocked(String userId) {
        Boolean isMember = redisTemplate.opsForSet().isMember(BLACKLIST_KEY,userId);
        return isMember != null && isMember;
    }

    public void addUserToBlacklist(String userId){
        redisTemplate.opsForSet().add(BLACKLIST_KEY,userId);
    }

    public void removeUserFromBlacklist(String userId){
        redisTemplate.opsForSet().remove(BLACKLIST_KEY,userId);
    }
}
