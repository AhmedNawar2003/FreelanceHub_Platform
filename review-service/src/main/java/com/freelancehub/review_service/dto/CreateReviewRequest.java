package com.freelancehub.review_service.dto;

import com.freelancehub.review_service.enums.ReviewType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateReviewRequest {

    @NotNull(message = "Contract ID is required")
    private Long contractId;

    @NotNull(message = "Reviewee ID is required")
    private Long revieweeId;

    @NotBlank(message = "Reviewee email is required")
    private String revieweeEmail;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Size(max = 500, message = "Comment must not exceed 500 characters")
    private String comment;

    @NotNull(message = "Review type is required")
    private ReviewType type;
}