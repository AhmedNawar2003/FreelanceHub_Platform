package com.freelancehub.notification_service.kafka;

import com.freelancehub.notification_service.config.KafkaConfig;
import com.freelancehub.notification_service.dto.NotificationEvent;
import com.freelancehub.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = KafkaConfig.NOTIFICATION_TOPIC,
            groupId = "notification-group"
    )
    public void consume(NotificationEvent event) {
        log.info("Received notification event: {}", event);
        notificationService.processNotification(event);
    }
}