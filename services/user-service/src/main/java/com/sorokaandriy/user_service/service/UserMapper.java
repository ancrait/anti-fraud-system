package com.sorokaandriy.user_service.service;

import com.sorokaandriy.user_service.dto.UpdateUserEntityRequest;
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

    public UserEntity fromUpdateToUser(UserEntity user, UpdateUserEntityRequest request) {
        user.setName(request.name());
        user.setSurname(request.surname());
        user.setBalance(request.balance());
        user.setRiskLevel(request.riskLevel());
        user.setDailyLimits(request.dailyLimits());

        return user;
    }
}
