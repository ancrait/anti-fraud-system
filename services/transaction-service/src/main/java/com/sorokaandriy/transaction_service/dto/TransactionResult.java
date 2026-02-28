package com.sorokaandriy.transaction_service.dto;

import com.sorokaandriy.transaction_service.model.TransactionalStatus;

public record TransactionResult(
        String transactionId,
        TransactionalStatus status
) {
}
