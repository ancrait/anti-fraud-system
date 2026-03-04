package com.sorokaandriy.transaction_service.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

@Configuration
public class KafkaTransactionTopicConfig {

    @Bean
    public NewTopic transactionTopic(){
        return TopicBuilder
                .name("raw-transactions")
                .partitions(3)
                .replicas(1)
                .configs(Map.of(
                        "retention.ms","604800000",
                        "retention.bytes","1073741824"
                ))
                .build();
    }
}
