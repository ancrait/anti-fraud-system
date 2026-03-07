package com.sorokaandriy.transaction_service.service;

import com.sorokaandriy.transaction_service.dto.TransactionRequest;
import com.sorokaandriy.transaction_service.dto.TransactionResult;
import com.sorokaandriy.transaction_service.exception.*;
import com.sorokaandriy.transaction_service.kafka.TransactionProducer;
import com.sorokaandriy.transaction_service.model.*;
import com.sorokaandriy.transaction_service.repository.TransactionRepository;
import com.sorokaandriy.transaction_service.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TransactionService {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;
    private final TransactionProducer transactionProducer;
    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final UserRedisTemplate userRedisTemplate;

    private final static int MAX_TRANSACTION_PER_MINUTE = 5;
    private final static String KEY_NAME = "rate_limit";

    public TransactionService(TransactionRepository repository, TransactionMapper mapper,
                              TransactionProducer transactionProducer, StringRedisTemplate redisTemplate, UserRepository userRepository, UserRedisTemplate userRedisTemplate) {
        this.repository = repository;
        this.mapper = mapper;
        this.transactionProducer = transactionProducer;
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
        this.userRedisTemplate = userRedisTemplate;
    }


    public String createTransaction(TransactionRequest request) {

        if (userRedisTemplate.isUserBlocked(request.userId())){
            throw new UserDeniedTransactionException(
                    "User deny transaction because of user status blocked");
        }

        UserEntity user = userRepository.findById(
                request.userId()).orElseThrow(() -> new UserNotFoundException(
                "User with id " + request.userId() + " not found"));

        if (user.getUserStatus() == UserStatus.BLOCKED){
            throw new UserDeniedTransactionException(
                    "User deny transaction because of user status blocked");
        }


        checkLastTransaction(user);

        checkDailyLimits(user,request.amount());

        limitTransaction(request,user);

        //save transaction

        if (user.getRiskLevel() == RiskLevel.LOW){
            TransactionEntity transaction = mapper.fromTransactionRequestToSuccessTransactionWithLevelRiskLow(request,user);
            updateBalance(user,request.amount());
            updateDailyLimits(user, request.amount());
            repository.save(transaction);
            return String.format("Transaction with id %s is approved", transaction.getId());
        }

        TransactionEntity transaction = mapper.fromTransactionRequestToSuccessTransaction(request,user);
        repository.save(transaction);
        transactionProducer.sendTransactionEvent(mapper.fromTransactionToEvent(transaction));
        return String.format("Transaction with id " + transaction.getId() + " has been processed");

    }

    private void updateBalance(UserEntity user, double amount) {
        if (user.getBalance().subtract(new BigDecimal(amount)).compareTo(BigDecimal.ZERO) >= 0){
            user.setBalance(user.getBalance().subtract(new BigDecimal(amount)));
            userRepository.save(user);
        }
        else {
            throw new BalanceNotEnoughException("You do not have enough funds in your account.");
        }
    }

    private void updateDailyLimits(UserEntity user, double amount) {
        if (user.getDailyLimits().subtract(new BigDecimal(amount)).compareTo(BigDecimal.ZERO) >= 0){
            user.setDailyLimits(user.getDailyLimits().subtract(new BigDecimal(amount)));
            userRepository.save(user);
        }
        else {
            throw new BalanceNotEnoughException("You do not have enough funds in your account.");
        }
    }


    private void limitTransaction(TransactionRequest request,UserEntity user){
        String key = KEY_NAME + request.userId();
        Long count = redisTemplate.opsForValue().increment(key);

        if (count != null && count == 1){
            redisTemplate.expire(key,60, TimeUnit.SECONDS);
        }

        if (count != null && count > MAX_TRANSACTION_PER_MINUTE){
            userRedisTemplate.addUserToBlacklist(user.getId());
            user.setUserStatus(UserStatus.BLOCKED);
            userRepository.save(user);
            throw new TransactionLimitException(
                    "You are blocked. Too many requests.");
        }

    }

    private void checkDailyLimits(UserEntity user, double currentAmount) {

        if (BigDecimal.valueOf(currentAmount).compareTo(user.getDailyLimits()) > 0) {
            throw new DailyLimitsException("Daily limit exhausted. Remaining: " + user.getDailyLimits());
        }
    }

    @Transactional
    public void transactionResult(TransactionResult transactionResult){

        log.info("Processing transaction result: ID={}, Status={}",
                transactionResult.transactionId(), transactionResult.status());

        TransactionEntity transaction = repository.findById(transactionResult.transactionId())
                .orElseThrow(() -> new TransactionalNotFoundException(
                        "Transaction with id " + transactionResult.transactionId() + " not found"));


        log.info("Found transaction: ID={}, Current Status={}, Amount={}",
                transaction.getId(), transaction.getStatus(), transaction.getAmount());

        if (transactionResult.status() == TransactionalStatus.APPROVED){
            transaction.setStatus(TransactionalStatus.APPROVED);

            UserEntity userEntity = transaction.getUserId();
            log.info("User before update: Balance={}, DailyLimit={}",
                    userEntity.getBalance(), userEntity.getDailyLimits());

            updateDailyLimits(userEntity,transaction.getAmount());
            log.info("Daily limits updated successfully");

            updateBalance(userEntity,transaction.getAmount());
            log.info("Balance updated successfully");

            repository.save(transaction);
            userRepository.save(userEntity);

            log.info("Approved transaction: ID={}", transaction.getId());
            log.info("User after update: Balance={}, DailyLimit={}",
                    userEntity.getBalance(), userEntity.getDailyLimits());
        }
        else if(transactionResult.status() == TransactionalStatus.REJECTED){
            transaction.setStatus(TransactionalStatus.REJECTED);
            log.info("rejected transaction");
            repository.save(transaction);
        }
    }

    private void checkLastTransaction(UserEntity user){

        Optional<TransactionEntity> lastTransaction = repository
                .findFirstByUserId_IdOrderByTimestampDesc(user.getId());

        if (lastTransaction.isEmpty()){
            return;
        }

        TransactionEntity transaction = lastTransaction.get();

        LocalDate convertIntoLocalDate = Instant.ofEpochMilli(transaction.getTimestamp())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();


        if (!convertIntoLocalDate.equals(LocalDate.now())){
            updateDailyLimits(user);
        }

    }

    private void updateDailyLimits(UserEntity user) {

         if (user.getRiskLevel() == RiskLevel.LOW){
             user.setDailyLimits(new BigDecimal("15000"));
         }
         else if (user.getRiskLevel() == RiskLevel.MEDIUM){
             user.setDailyLimits(new BigDecimal("10000"));
         }
         else {
             user.setDailyLimits(new BigDecimal("5000"));
         }

         userRepository.save(user);

    }


}
