package com.sorokaandriy.transaction_service.kafka;

import com.sorokaandriy.transaction_service.dto.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionProducer {

    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    public TransactionProducer(KafkaTemplate<String, TransactionEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTransactionEvent(TransactionEvent transactionEvent){
        log.info("sending transaction {}",transactionEvent);
        Message<TransactionEvent> message = MessageBuilder
                .withPayload(transactionEvent)
                .setHeader(KafkaHeaders.TOPIC,"raw-transactions")
                .build();
        kafkaTemplate.send(message);
    }
}
