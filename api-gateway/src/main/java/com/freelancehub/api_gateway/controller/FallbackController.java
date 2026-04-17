package com.freelancehub.api_gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/user-service")
    public ResponseEntity<Map<String, Object>> userServiceFallback() {
        return buildFallbackResponse("user-service");
    }

    @GetMapping("/project-service")
    public ResponseEntity<Map<String, Object>> projectServiceFallback() {
        return buildFallbackResponse("project-service");
    }

    @GetMapping("/contract-service")
    public ResponseEntity<Map<String, Object>> contractServiceFallback() {
        return buildFallbackResponse("contract-service");
    }

    @GetMapping("/payment-service")
    public ResponseEntity<Map<String, Object>> paymentServiceFallback() {
        return buildFallbackResponse("payment-service");
    }

    @GetMapping("/review-service")
    public ResponseEntity<Map<String, Object>> reviewServiceFallback() {
        return buildFallbackResponse("review-service");
    }

    @GetMapping("/notification-service")
    public ResponseEntity<Map<String, Object>> notificationServiceFallback() {
        return buildFallbackResponse("notification-service");
    }

    private ResponseEntity<Map<String, Object>> buildFallbackResponse(String service) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "status", 503,
                        "error", "Service Unavailable",
                        "message", service + " is currently unavailable. Please try again later.",
                        "service", service
                ));
    }
}