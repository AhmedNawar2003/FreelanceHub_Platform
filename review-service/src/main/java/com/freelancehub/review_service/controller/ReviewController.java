package com.freelancehub.review_service.controller;

import com.freelancehub.review_service.dto.CreateReviewRequest;
import com.freelancehub.review_service.dto.ReviewResponse;
import com.freelancehub.review_service.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Reviews", description = "Review and rating management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "Create a review",
            description = "Submit a review after contract completion")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Review created"),
            @ApiResponse(responseCode = "400", description = "Already reviewed or self-review")
    })
    public ResponseEntity<ReviewResponse> createReview(
            @Valid @RequestBody CreateReviewRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.createReview(request, token.substring(7)));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get reviews for a user",
            description = "Returns all reviews received by a user")
    @ApiResponse(responseCode = "200", description = "Reviews retrieved")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUser(
            @PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewsByReviewee(userId));
    }

    @GetMapping("/contract/{contractId}")
    @Operation(summary = "Get reviews by contract",
            description = "Returns all reviews for a specific contract")
    @ApiResponse(responseCode = "200", description = "Reviews retrieved")
    public ResponseEntity<List<ReviewResponse>> getReviewsByContract(
            @PathVariable Long contractId) {
        return ResponseEntity.ok(reviewService.getReviewsByContract(contractId));
    }

    @GetMapping("/my")
    @Operation(summary = "Get my given reviews",
            description = "Returns all reviews submitted by the authenticated user")
    @ApiResponse(responseCode = "200", description = "Reviews retrieved")
    public ResponseEntity<List<ReviewResponse>> getMyReviews(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(reviewService.getMyReviews(token.substring(7)));
    }

    @GetMapping("/stats/{userId}")
    @Operation(summary = "Get user rating stats",
            description = "Returns average rating and star breakdown for a user")
    @ApiResponse(responseCode = "200", description = "Stats retrieved")
    public ResponseEntity<Map<String, Object>> getUserStats(
            @PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getUserStats(userId));
    }
}