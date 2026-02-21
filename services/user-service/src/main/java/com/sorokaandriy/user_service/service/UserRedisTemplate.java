package com.sorokaandriy.user_service.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserRedisTemplate {

    private final StringRedisTemplate redisTemplate;
    private final static String BLACKLIST_KEY = "blacklisted_users";

    public UserRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addUserToBlacklist(String userId){
        redisTemplate.opsForSet().add(BLACKLIST_KEY,userId);
    }

    public void removeUserFromBlacklist(String userId){
        redisTemplate.opsForSet().remove(BLACKLIST_KEY,userId);
    }
}
