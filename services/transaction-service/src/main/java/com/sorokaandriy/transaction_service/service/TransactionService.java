package com.sorokaandriy.transaction_service.service;

import com.sorokaandriy.transaction_service.dto.TransactionRequest;
import com.sorokaandriy.transaction_service.exception.*;
import com.sorokaandriy.transaction_service.model.*;
import com.sorokaandriy.transaction_service.repository.TransactionRepository;
import com.sorokaandriy.transaction_service.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;
    private final TransactionProducer transactionProducer;
    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;

    private final static int MAX_TRANSACTION_PER_MINUTE = 5;
    private final static String KEY_NAME = "rate_limit";

    public TransactionService(TransactionRepository repository, TransactionMapper mapper,
                              TransactionProducer transactionProducer, StringRedisTemplate redisTemplate, UserRepository userRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.transactionProducer = transactionProducer;
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
    }

    public String createTransaction(@Valid TransactionRequest request) {

        UserEntity user = userRepository.findById(
                request.userId()).orElseThrow(() -> new UserNotFoundException(
                "User with id " + request.userId() + " not found"));


        if (user.getUserStatus() == UserStatus.BLOCKED){
            throw new UserDeniedTransactionException(
                    "User deny transaction because of user status " + user.getUserStatus());
        }

        raiseDailyLimits(user);

        checkDailyLimits(user,request.amount());

        limitTransaction(request,user);

        //save transaction

        if (user.getRiskLevel() == RiskLevel.LOW){
            TransactionEntity transaction = mapper.fromTransactionRequestToSuccessTransactionWithLevelRiskLow(request,user);
            repository.save(transaction);
            return String.format("Transaction with id %s is approved", transaction.getId());
        }

        TransactionEntity transaction = mapper.fromTransactionRequestToSuccessTransaction(request,user);
        repository.save(transaction);
        transactionProducer.sendTransactionEvent(mapper.fromTransactionToEvent(transaction));
        return String.format("Transaction with id %s is pending", transaction.getId());

    }


    private void raiseDailyLimits(UserEntity user){
        if (user.getRiskLevel() == RiskLevel.LOW){
            user.setDailyLimits(user.getDailyLimits().multiply(new BigDecimal("1.5")));
        }
        else {
            user.setDailyLimits(user.getDailyLimits().divide(new BigDecimal("1.5")));
        }

        userRepository.save(user);
    }


    private void limitTransaction(TransactionRequest request,UserEntity user){
        String key = KEY_NAME + request.userId();
        Long count = redisTemplate.opsForValue().increment(key);

        if (count != null && count == 1){
            redisTemplate.expire(key,60, TimeUnit.SECONDS);
        }

        if (count != null && count > MAX_TRANSACTION_PER_MINUTE){
            user.setUserStatus(UserStatus.BLOCKED);
            userRepository.save(user);
            throw new TransactionLimitException(
                    "You are blocked. Too many requests. ");
        }

    }

    private void checkDailyLimits(UserEntity user, double currentAmount) {
        List<TransactionEntity> transactions = repository.findAllByUserId_Id(user.getId());
        LocalDate today = LocalDate.now();

        double sumOfTodayTransactions = transactions.stream()
                .filter(t -> LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getTimestamp()),
                        ZoneId.systemDefault()).toLocalDate().equals(today))
                .filter(t -> t.getStatus() != TransactionalStatus.REJECTED)
                .mapToDouble(TransactionEntity::getAmount)
                .sum();

        if (sumOfTodayTransactions + currentAmount > user.getDailyLimits().doubleValue()) {
            throw new DailyLimitsException("Daily limit exhausted");
        }
    }


}
