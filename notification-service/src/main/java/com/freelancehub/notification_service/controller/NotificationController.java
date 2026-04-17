package com.freelancehub.notification_service.controller;

import com.freelancehub.notification_service.dto.NotificationEvent;
import com.freelancehub.notification_service.dto.NotificationResponse;
import com.freelancehub.notification_service.kafka.NotificationProducer;
import com.freelancehub.notification_service.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Real-time notification management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationProducer notificationProducer;

    @GetMapping
    @Operation(summary = "Get all my notifications",
            description = "Returns all notifications ordered by date desc")
    @ApiResponse(responseCode = "200", description = "Notifications retrieved")
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(
                notificationService.getMyNotifications(token.substring(7)));
    }

    @GetMapping("/unread")
    @Operation(summary = "Get unread notifications",
            description = "Returns only unread notifications")
    @ApiResponse(responseCode = "200", description = "Unread notifications retrieved")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(token.substring(7)));
    }

    @GetMapping("/count")
    @Operation(summary = "Get unread count",
            description = "Returns the number of unread notifications")
    @ApiResponse(responseCode = "200", description = "Count retrieved")
    public ResponseEntity<Map<String, Object>> getNotificationCount(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(
                notificationService.getNotificationCount(token.substring(7)));
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Mark notification as read")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Marked as read"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        notificationService.markAsRead(id, token.substring(7));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all")
    @Operation(summary = "Mark all notifications as read")
    @ApiResponse(responseCode = "200", description = "All marked as read")
    public ResponseEntity<Void> markAllAsRead(
            @RequestHeader("Authorization") String token) {
        notificationService.markAllAsRead(token.substring(7));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send")
    @Operation(summary = "Send test notification",
            description = "Test endpoint to send notification via Kafka (dev only)")
    @ApiResponse(responseCode = "200", description = "Notification sent")
    public ResponseEntity<Void> sendNotification(
            @RequestBody NotificationEvent event) {
        notificationProducer.sendNotification(event);
        return ResponseEntity.ok().build();
    }
}