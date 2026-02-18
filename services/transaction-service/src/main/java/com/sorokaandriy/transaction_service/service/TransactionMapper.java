package com.sorokaandriy.transaction_service.service;

import com.sorokaandriy.transaction_service.dto.TransactionEvent;
import com.sorokaandriy.transaction_service.dto.TransactionRequest;
import com.sorokaandriy.transaction_service.model.TransactionEntity;
import com.sorokaandriy.transaction_service.model.TransactionalStatus;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionMapper {
    public TransactionEntity fromTransactionRequestToTransaction(
            @Valid TransactionRequest request) {
        return TransactionEntity.builder()
                .id(UUID.randomUUID().toString())
                .userId(request.userId())
                .amount(request.amount())
                .timestamp(System.currentTimeMillis())
                .status(TransactionalStatus.PENDING)
                .build();
    }

    public TransactionEvent fromTransactionToEvent(TransactionEntity transaction) {
        return TransactionEvent.builder()
                .id(transaction.getId())
                .userId(transaction.getUserId())
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .timestamp(transaction.getTimestamp())
                .build();
    }
}
