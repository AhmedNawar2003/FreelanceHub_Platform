package com.freelancehub.notification_service.kafka;

import com.freelancehub.notification_service.config.KafkaConfig;
import com.freelancehub.notification_service.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public void sendNotification(NotificationEvent event) {
        log.info("Sending notification event: {}", event);
        kafkaTemplate.send(KafkaConfig.NOTIFICATION_TOPIC,
                event.getUserEmail(), event);
    }
}