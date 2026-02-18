package com.sorokaandriy.transaction_service.dto;

import com.sorokaandriy.transaction_service.model.TransactionalStatus;
import com.sorokaandriy.transaction_service.model.UserEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;

@Builder
public record TransactionEvent(
         String id,
         UserEntity userId,
         double amount,
         Long timestamp,
         TransactionalStatus status
) {
}
