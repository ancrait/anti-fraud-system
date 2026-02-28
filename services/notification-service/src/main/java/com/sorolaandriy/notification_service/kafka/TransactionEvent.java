package com.sorolaandriy.notification_service.kafka;

import com.sorolaandriy.notification_service.dto.TransactionalStatus;
import lombok.Builder;

@Builder
public record TransactionEvent(
    String transactionId,
    TransactionalStatus status
) {
}
