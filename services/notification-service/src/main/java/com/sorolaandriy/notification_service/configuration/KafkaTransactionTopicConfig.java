package com.sorolaandriy.notification_service.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTransactionTopicConfig {

    @Bean
    public NewTopic transactionResult(){
        return TopicBuilder
                .name("transaction-result")
                .build();
    }
}
