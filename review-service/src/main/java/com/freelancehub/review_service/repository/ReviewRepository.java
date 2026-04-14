package com.freelancehub.review_service.repository;

import com.freelancehub.review_service.entity.Review;
import com.freelancehub.review_service.enums.ReviewType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRevieweeId(Long revieweeId);
    List<Review> findByReviewerId(Long reviewerId);
    List<Review> findByContractId(Long contractId);
    Optional<Review> findByContractIdAndReviewerIdAndType(
            Long contractId, Long reviewerId, ReviewType type);
    boolean existsByContractIdAndReviewerIdAndType(
            Long contractId, Long reviewerId, ReviewType type);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.revieweeId = :revieweeId")
    Double getAverageRatingByRevieweeId(Long revieweeId);
}