package com.sorolaandriy.notification_service.service;

import com.sorolaandriy.notification_service.dto.TransactionalStatus;
import com.sorolaandriy.notification_service.exception.TokenTimeOutException;
import com.sorolaandriy.notification_service.kafka.TransactionEvent;
import com.sorolaandriy.notification_service.kafka.TransactionProducer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionApprovedService {

    private final StringRedisTemplate redisTemplate;
    private final TransactionProducer producer;

    public TransactionApprovedService(StringRedisTemplate redisTemplate, TransactionProducer producer) {
        this.redisTemplate = redisTemplate;
        this.producer = producer;
    }

    public String approvedTransaction(String token) {

        String transactionId = redisTemplate.opsForValue().get(token);

        if (transactionId == null){
            throw new TokenTimeOutException("Token expired");
        }

        TransactionEvent transactionEvent = TransactionEvent.builder()
                .transactionId(transactionId)
                .status(TransactionalStatus.APPROVED)
                .build();

        producer.sendTransactionEvent(transactionEvent);

        redisTemplate.delete(token);

        return "Transaction with id " + transactionId + " is approved";






    }
}
