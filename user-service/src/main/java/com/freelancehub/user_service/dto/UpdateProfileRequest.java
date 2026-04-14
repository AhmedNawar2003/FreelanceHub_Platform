package com.freelancehub.user_service.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters")
    private String fullName;

    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;

    @Size(max = 200, message = "Skills must not exceed 200 characters")
    private String skills;

    @Size(max = 200, message = "Portfolio URL must not exceed 200 characters")
    private String portfolioUrl;
}