package com.freelancehub.project_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class BidRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.0", message = "Amount must be at least 1")
    private BigDecimal amount;

    @NotBlank(message = "Proposal is required")
    @Size(min = 30, message = "Proposal must be at least 30 characters")
    private String proposal;
}
