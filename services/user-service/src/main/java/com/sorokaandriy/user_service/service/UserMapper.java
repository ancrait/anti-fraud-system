package com.sorokaandriy.user_service.service;

import com.sorokaandriy.user_service.dto.UpdateUserEntityRequest;
import com.sorokaandriy.user_service.dto.UserEntityRequest;
import com.sorokaandriy.user_service.model.RiskLevel;
import com.sorokaandriy.user_service.model.UserEntity;
import com.sorokaandriy.user_service.model.UserStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class UserMapper {

    public UserEntity fromUserRequestToUser(UserEntityRequest request) {

        BigDecimal dailyLimits = null;
        if (request.riskLevel() == RiskLevel.LOW){
            dailyLimits = new BigDecimal("15000");
        }else if (request.riskLevel() == RiskLevel.MEDIUM){
            dailyLimits = new BigDecimal("10000");
        }else if (request.riskLevel() == RiskLevel.HIGH){
            dailyLimits = new BigDecimal("5000");
        }

        return UserEntity.builder()
                .id(UUID.randomUUID().toString())
                .name(request.name())
                .surname(request.surname())
                .balance(request.balance())
                .email(request.email())
                .userStatus(UserStatus.ACTIVE)
                .riskLevel(request.riskLevel())
                .dailyLimits(dailyLimits)
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
