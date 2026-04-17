package com.freelancehub.notification_service.service;

import com.freelancehub.notification_service.dto.NotificationEvent;
import com.freelancehub.notification_service.dto.NotificationResponse;
import com.freelancehub.notification_service.entity.Notification;
import com.freelancehub.notification_service.repository.NotificationRepository;
import com.freelancehub.notification_service.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtService jwtService;

    public void processNotification(NotificationEvent event) {
        // Save to DB
        Notification notification = Notification.builder()
                .userId(event.getUserId())
                .userEmail(event.getUserEmail())
                .title(event.getTitle())
                .message(event.getMessage())
                .type(event.getType())
                .referenceId(event.getReferenceId())
                .build();

        Notification saved = notificationRepository.save(notification);
        log.info("Notification saved: {}", saved.getId());

        // Send via WebSocket
        NotificationResponse response = mapToResponse(saved);
        messagingTemplate.convertAndSend("/topic/all-notifications", response);
        messagingTemplate.convertAndSendToUser(
                event.getUserEmail(),
                "/queue/notifications",
                response
        );

        log.info("WebSocket notification sent to: {}", event.getUserEmail());
    }

    public List<NotificationResponse> getMyNotifications(String token) {
        Long userId = jwtService.extractUserId(token);
        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::mapToResponse).toList();
    }

    public List<NotificationResponse> getUnreadNotifications(String token) {
        Long userId = jwtService.extractUserId(token);
        return notificationRepository
                .findByUserIdAndReadFalseOrderByCreatedAtDesc(userId)
                .stream().map(this::mapToResponse).toList();
    }

    public Map<String, Object> getNotificationCount(String token) {
        Long userId = jwtService.extractUserId(token);
        long unreadCount = notificationRepository.countByUserIdAndReadFalse(userId);
        return Map.of("unreadCount", unreadCount);
    }

    @Transactional
    public void markAsRead(Long notificationId, String token) {
        Long userId = jwtService.extractUserId(token);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getUserId().equals(userId))
            throw new RuntimeException("Not authorized");

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead(String token) {
        Long userId = jwtService.extractUserId(token);
        notificationRepository.markAllAsReadByUserId(userId);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .userEmail(notification.getUserEmail())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .read(notification.isRead())
                .referenceId(notification.getReferenceId())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}