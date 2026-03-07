package com.sorokaandriy.transaction_service.kafka;

import com.sorokaandriy.transaction_service.dto.TransactionEvent;
import com.sorokaandriy.transaction_service.dto.TransactionResult;
import com.sorokaandriy.transaction_service.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionConsumer {

    private final TransactionService service;

    public TransactionConsumer(TransactionService service) {
        this.service = service;
    }

    @KafkaListener(topics = "transaction-result", groupId = "transaction-group")
    public void getTransactionResult(TransactionResult transactionResult){
        log.info("Received message from Kafka: {}", transactionResult);
        log.info("Transaction ID: {}, Status: {}",
                transactionResult.transactionId(),
                transactionResult.status());
        service.transactionResult(transactionResult);
    }
}
