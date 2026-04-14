package com.freelancehub.review_service.controller;

import com.freelancehub.review_service.dto.CreateReviewRequest;
import com.freelancehub.review_service.dto.ReviewResponse;
import com.freelancehub.review_service.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @Valid @RequestBody CreateReviewRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.createReview(request, token.substring(7)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUser(
            @PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewsByReviewee(userId));
    }

    @GetMapping("/contract/{contractId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByContract(
            @PathVariable Long contractId) {
        return ResponseEntity.ok(reviewService.getReviewsByContract(contractId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReviewResponse>> getMyReviews(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(reviewService.getMyReviews(token.substring(7)));
    }

    @GetMapping("/stats/{userId}")
    public ResponseEntity<Map<String, Object>> getUserStats(
            @PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getUserStats(userId));
    }
}