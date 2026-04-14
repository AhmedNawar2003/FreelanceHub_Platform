package com.freelancehub.payment_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateEscrowRequest {

    @NotNull(message = "Contract ID is required")
    private Long contractId;

    @NotNull(message = "Freelancer ID is required")
    private Long freelancerId;

    @NotBlank(message = "Freelancer email is required")
    private String freelancerEmail;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "1.0", message = "Amount must be at least 1")
    private BigDecimal totalAmount;
}
