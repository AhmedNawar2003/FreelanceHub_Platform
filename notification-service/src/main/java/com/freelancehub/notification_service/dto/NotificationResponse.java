package com.freelancehub.notification_service.dto;

import com.freelancehub.notification_service.enums.NotificationType;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {
    private Long id;
    private Long userId;
    private String userEmail;
    private String title;
    private String message;
    private NotificationType type;
    private boolean read;
    private Long referenceId;
    private LocalDateTime createdAt;
}
