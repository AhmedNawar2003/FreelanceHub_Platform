package com.freelancehub.project_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateProjectRequest {

    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    @Size(min = 20, message = "Description must be at least 20 characters")
    private String description;

    @DecimalMin(value = "1.0", message = "Budget must be at least 1")
    private BigDecimal budget;

    private String skills;
}