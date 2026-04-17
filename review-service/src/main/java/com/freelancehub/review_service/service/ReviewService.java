package com.freelancehub.review_service.service;

import com.freelancehub.review_service.dto.CreateReviewRequest;
import com.freelancehub.review_service.dto.ReviewResponse;
import com.freelancehub.review_service.entity.Review;
import com.freelancehub.review_service.repository.ReviewRepository;
import com.freelancehub.review_service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final JwtService jwtService;

    @CacheEvict(value = {"reviews", "review-stats"}, allEntries = true)
    public ReviewResponse createReview(CreateReviewRequest request, String token) {
        String reviewerEmail = jwtService.extractEmail(token);
        Long reviewerId = jwtService.extractUserId(token);

        if (reviewRepository.existsByContractIdAndReviewerIdAndType(
                request.getContractId(), reviewerId, request.getType()))
            throw new RuntimeException("You have already reviewed this contract");

        if (reviewerId.equals(request.getRevieweeId()))
            throw new RuntimeException("You cannot review yourself");

        Review review = Review.builder()
                .contractId(request.getContractId())
                .reviewerId(reviewerId)
                .reviewerEmail(reviewerEmail)
                .revieweeId(request.getRevieweeId())
                .revieweeEmail(request.getRevieweeEmail())
                .rating(request.getRating())
                .comment(request.getComment())
                .type(request.getType())
                .build();

        return mapToResponse(reviewRepository.save(review));
    }

    @Cacheable(value = "reviews", key = "'user:' + #revieweeId")
    public List<ReviewResponse> getReviewsByReviewee(Long revieweeId) {
        return reviewRepository.findByRevieweeId(revieweeId)
                .stream().map(this::mapToResponse).toList();
    }

    @Cacheable(value = "reviews", key = "'contract:' + #contractId")
    public List<ReviewResponse> getReviewsByContract(Long contractId) {
        return reviewRepository.findByContractId(contractId)
                .stream().map(this::mapToResponse).toList();
    }

    public List<ReviewResponse> getMyReviews(String token) {
        return reviewRepository.findByReviewerId(jwtService.extractUserId(token))
                .stream().map(this::mapToResponse).toList();
    }

    @Cacheable(value = "review-stats", key = "#userId")
    public Map<String, Object> getUserStats(Long userId) {
        List<Review> reviews = reviewRepository.findByRevieweeId(userId);
        Double avgRating = reviewRepository.getAverageRatingByRevieweeId(userId);

        return Map.of(
                "totalReviews", reviews.size(),
                "averageRating", avgRating != null ?
                        Math.round(avgRating * 10.0) / 10.0 : 0.0,
                "ratings", Map.of(
                        "5stars", reviews.stream().filter(r -> r.getRating() == 5).count(),
                        "4stars", reviews.stream().filter(r -> r.getRating() == 4).count(),
                        "3stars", reviews.stream().filter(r -> r.getRating() == 3).count(),
                        "2stars", reviews.stream().filter(r -> r.getRating() == 2).count(),
                        "1stars", reviews.stream().filter(r -> r.getRating() == 1).count()
                )
        );
    }

    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .contractId(review.getContractId())
                .reviewerId(review.getReviewerId())
                .reviewerEmail(review.getReviewerEmail())
                .revieweeId(review.getRevieweeId())
                .revieweeEmail(review.getRevieweeEmail())
                .rating(review.getRating())
                .comment(review.getComment())
                .type(review.getType())
                .createdAt(review.getCreatedAt())
                .build();
    }
}