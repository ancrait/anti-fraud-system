package com.sorokaandriy.user_service.service;

import com.sorokaandriy.user_service.dto.UpdateUserEntityRequest;
import com.sorokaandriy.user_service.dto.UserEntityRequest;
import com.sorokaandriy.user_service.model.UserEntity;
import com.sorokaandriy.user_service.model.UserStatus;
import com.sorokaandriy.user_service.repository.UserRepository;
import jakarta.persistence.Id;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import com.sorokaandriy.user_service.exception.UserNotFoundException;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final UserRedisTemplate redisTemplate;

    public UserService(UserRepository repository, UserMapper mapper, UserRedisTemplate redisTemplate) {
        this.repository = repository;
        this.mapper = mapper;
        this.redisTemplate = redisTemplate;
    }


    public @Nullable UserEntity createUser(UserEntityRequest request) {
        UserEntity user = mapper.fromUserRequestToUser(request);
        return repository.save(user);
    }

    public @Nullable UserEntity updateUser(UpdateUserEntityRequest request, String userId) {
        UserEntity user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " +
                userId + " not found"));


        return repository.save(mapper.fromUpdateToUser(user,request));
    }

    public @Nullable UserEntity updateStatus(String userId, String userStatus) {
        UserEntity user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " +
                        userId + " not found"));

        user.setUserStatus(UserStatus.valueOf(userStatus));

        if(UserStatus.valueOf(userStatus) == UserStatus.BLOCKED){
            redisTemplate.addUserToBlacklist(userId);
        }

        else if(UserStatus.valueOf(userStatus) == UserStatus.ACTIVE){
            redisTemplate.removeUserFromBlacklist(userId);
        }

            return repository.save(user);
    }
}
