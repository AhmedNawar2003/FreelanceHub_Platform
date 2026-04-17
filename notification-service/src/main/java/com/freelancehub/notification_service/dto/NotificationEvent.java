package com.freelancehub.notification_service.dto;

import com.freelancehub.notification_service.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private Long userId;
    private String userEmail;
    private String title;
    private String message;
    private NotificationType type;
    private Long referenceId;
}