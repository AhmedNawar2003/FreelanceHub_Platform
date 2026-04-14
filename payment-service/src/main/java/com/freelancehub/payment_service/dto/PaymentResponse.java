package com.freelancehub.payment_service.dto;

import com.freelancehub.payment_service.enums.EscrowStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PaymentResponse {
    private Long id;
    private Long contractId;
    private Long clientId;
    private String clientEmail;
    private Long freelancerId;
    private String freelancerEmail;
    private BigDecimal totalAmount;
    private BigDecimal releasedAmount;
    private BigDecimal remainingAmount;
    private EscrowStatus status;
    private LocalDateTime createdAt;
    private List<TransactionResponse> transactions;
}
