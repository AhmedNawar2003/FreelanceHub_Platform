package com.freelancehub.project_service.dto;

import com.freelancehub.project_service.enums.BidStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BidResponse {
    private Long id;
    private Long freelancerId;
    private String freelancerEmail;
    private BigDecimal amount;
    private String proposal;
    private BidStatus status;
    private LocalDateTime createdAt;
}