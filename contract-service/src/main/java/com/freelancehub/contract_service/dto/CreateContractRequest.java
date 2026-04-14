package com.freelancehub.contract_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateContractRequest {

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "Freelancer ID is required")
    private Long freelancerId;

    @NotNull(message = "Freelancer email is required")
    private String freelancerEmail;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "1.0", message = "Amount must be at least 1")
    private BigDecimal totalAmount;

    private List<MilestoneRequest> milestones;
}