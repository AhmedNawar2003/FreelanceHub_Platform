package com.freelancehub.contract_service.dto;

import com.freelancehub.contract_service.enums.ContractStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ContractResponse {
    private Long id;
    private Long projectId;
    private Long clientId;
    private String clientEmail;
    private Long freelancerId;
    private String freelancerEmail;
    private BigDecimal totalAmount;
    private ContractStatus status;
    private LocalDateTime createdAt;
    private List<MilestoneResponse> milestones;
}