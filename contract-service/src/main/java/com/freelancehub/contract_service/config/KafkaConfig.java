package com.freelancehub.contract_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String CONTRACT_CREATED_TOPIC = "contract-created";
    public static final String PAYMENT_FUNDED_TOPIC = "payment-funded";
    public static final String SAGA_ROLLBACK_TOPIC = "saga-rollback";

    @Bean
    public NewTopic contractCreatedTopic() {
        return TopicBuilder.name(CONTRACT_CREATED_TOPIC)
                .partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic paymentFundedTopic() {
        return TopicBuilder.name(PAYMENT_FUNDED_TOPIC)
                .partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic sagaRollbackTopic() {
        return TopicBuilder.name(SAGA_ROLLBACK_TOPIC)
                .partitions(3).replicas(1).build();
    }
}