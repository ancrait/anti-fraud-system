package com.sorolaandriy.notification_service.kafka;

import com.sorolaandriy.notification_service.dto.TransactionalStatus;
import com.sorolaandriy.notification_service.dto.UserEntity;

public record TransactionConfirmation(

        String id,
        UserEntity userId,
        double amount,
        Long timestamp,
        TransactionalStatus status

) {

}
