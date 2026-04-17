package com.freelancehub.contract_service.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SagaEvent {
    private String sagaId;
    private String eventType;
    private String status;
    private Long contractId;
    private Long projectId;
    private Long clientId;
    private String clientEmail;
    private Long freelancerId;
    private String freelancerEmail;
    private BigDecimal amount;
    private String failureReason;
}