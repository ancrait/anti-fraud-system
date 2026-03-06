package com.sorolaandriy.notification_service.kafka;

import com.sorolaandriy.notification_service.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class NotificationConsumer {

    private final NotificationService notificationService;


    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "raw-transactions", groupId = "notification-group")
    public void consumeTransaction(TransactionConfirmation confirmation){
        log.info("Consuming transaction from kafka {}", confirmation);
        notificationService.sendTransactionConfirmation(
                confirmation.id(),
                confirmation.userId().email(),
                confirmation.userId().name(),
                confirmation.userId().surname(),
                confirmation.amount(),
                UUID.randomUUID().toString()
                );

    }
}
