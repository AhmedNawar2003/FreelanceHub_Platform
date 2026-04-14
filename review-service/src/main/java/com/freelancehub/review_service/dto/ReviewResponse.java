package com.freelancehub.review_service.dto;

import com.freelancehub.review_service.enums.ReviewType;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ReviewResponse {
    private Long id;
    private Long contractId;
    private Long reviewerId;
    private String reviewerEmail;
    private Long revieweeId;
    private String revieweeEmail;
    private int rating;
    private String comment;
    private ReviewType type;
    private LocalDateTime createdAt;
}