package com.sorokaandriy.transaction_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sorokaandriy.transaction_service.model.TransactionalStatus;

public record TransactionResult(
        @JsonProperty("transactionId")
        String transactionId,
        @JsonProperty("status")
        TransactionalStatus status
) {
}
