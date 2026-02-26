package com.sorokaandriy.transaction_service.service;

import com.sorokaandriy.transaction_service.dto.TransactionEvent;
import com.sorokaandriy.transaction_service.dto.TransactionRequest;
import com.sorokaandriy.transaction_service.model.TransactionEntity;
import com.sorokaandriy.transaction_service.model.TransactionalStatus;
import com.sorokaandriy.transaction_service.model.UserEntity;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionMapper {
    
    public TransactionEntity fromTransactionRequestToSuccessTransactionWithLevelRiskLow(
            @Valid TransactionRequest request, UserEntity user) {
        return TransactionEntity.builder()
                .id(UUID.randomUUID().toString())
                .userId(user)
                .amount(request.amount())
                .timestamp(System.currentTimeMillis())
                .status(TransactionalStatus.APPROVED)
                .build();
    }

    public TransactionEntity fromTransactionRequestToSuccessTransaction(
            @Valid TransactionRequest request, UserEntity user) {
        return TransactionEntity.builder()
                .id(UUID.randomUUID().toString())
                .userId(user)
                .amount(request.amount())
                .timestamp(System.currentTimeMillis())
                .status(TransactionalStatus.PENDING)
                .build();
    }


    public TransactionEntity fromTransactionRequestToRejectedTransaction(
            @Valid TransactionRequest request, UserEntity user) {
        return TransactionEntity.builder()
                .id(UUID.randomUUID().toString())
                .userId(user)
                .amount(request.amount())
                .timestamp(System.currentTimeMillis())
                .status(TransactionalStatus.REJECTED)
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
