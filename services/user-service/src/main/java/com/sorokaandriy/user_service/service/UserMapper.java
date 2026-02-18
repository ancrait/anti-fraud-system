package com.sorokaandriy.user_service.service;

import com.sorokaandriy.user_service.dto.UserEntityRequest;
import com.sorokaandriy.user_service.model.UserEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserMapper {

    public UserEntity fromUserRequestToUser(UserEntityRequest request) {
        return UserEntity.builder()
                .id(UUID.randomUUID().toString())
                .name(request.name())
                .surname(request.surname())
                .balance(request.balance())
                .email(request.email())
                .build();
    }
}
