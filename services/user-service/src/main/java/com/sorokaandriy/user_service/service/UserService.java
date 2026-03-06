package com.sorokaandriy.user_service.service;

import com.sorokaandriy.user_service.dto.UpdateUserEntityRequest;
import com.sorokaandriy.user_service.dto.UserEntityRequest;
import com.sorokaandriy.user_service.model.UserEntity;
import com.sorokaandriy.user_service.model.UserStatus;
import com.sorokaandriy.user_service.repository.UserRepository;
import jakarta.persistence.Id;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.data.autoconfigure.web.DataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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



    public @Nullable Page<UserEntity> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page,size,Sort.by(sortBy));
        return repository.findAll(pageable);
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

    public @Nullable UserEntity updateStatus(String userId, UserStatus userStatus) {
        UserEntity user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " +
                        userId + " not found"));

        user.setUserStatus(userStatus);

        if(userStatus == UserStatus.BLOCKED){
            redisTemplate.addUserToBlacklist(userId);
        }

        else if(userStatus == UserStatus.ACTIVE){
            redisTemplate.removeUserFromBlacklist(userId);
        }

            return repository.save(user);
    }

    public @Nullable String deleteUser(String userId) {
        UserEntity user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " +
                        userId + " not found"));
        repository.delete(user);
        return "User with id " + userId + " was successfully deleted";
    }


}
