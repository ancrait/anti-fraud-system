package com.sorolaandriy.notification_service.service;

import com.sorolaandriy.notification_service.dto.TransactionalStatus;
import com.sorolaandriy.notification_service.exception.TokenTimeOutException;
import com.sorolaandriy.notification_service.kafka.TransactionEvent;
import com.sorolaandriy.notification_service.kafka.TransactionProducer;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionApprovedService {

    private final StringRedisTemplate redisTemplate;
    private final TransactionProducer producer;

    public TransactionApprovedService(StringRedisTemplate redisTemplate, TransactionProducer producer) {
        this.redisTemplate = redisTemplate;
        this.producer = producer;
    }

    public String approveTransaction(String token) {

        String transactionId = redisTemplate.opsForValue().get(token);

        if (transactionId == null){
            throw new TokenTimeOutException("Token expired");
        }

        sendEvent(transactionId,TransactionalStatus.APPROVED);

        redisTemplate.delete(token);

        return "Transaction with id " + transactionId + " is approved";
    }

    public String rejectTransaction(String token) {

        String transactionId = redisTemplate.opsForValue().get(token);

        if (transactionId == null){
            throw new TokenTimeOutException("Token expired");
        }

        sendEvent(transactionId,TransactionalStatus.REJECTED);

        redisTemplate.delete(token);

        return "Transaction with id " + transactionId + " is rejected";

    }

    private void sendEvent(String transactionId, TransactionalStatus status){

        TransactionEvent transactionEvent = TransactionEvent.builder()
                .transactionId(transactionId)
                .status(status)
                .build();
        log.info("sending transaction to transaction service {}",transactionEvent);

        producer.sendTransactionEvent(transactionEvent);


    }
}
