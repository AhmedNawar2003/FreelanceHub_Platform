package com.freelancehub.contract_service.dto;

import com.freelancehub.contract_service.enums.MilestoneStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class MilestoneResponse {
    private Long id;
    private String title;
    private String description;
    private BigDecimal amount;
    private MilestoneStatus status;
    private String submissionNote;
    private LocalDateTime createdAt;
}