package com.freelancehub.payment_service.saga;

import com.freelancehub.payment_service.dto.SagaEvent;
import com.freelancehub.payment_service.entity.Escrow;
import com.freelancehub.payment_service.entity.Transaction;
import com.freelancehub.payment_service.enums.EscrowStatus;
import com.freelancehub.payment_service.enums.TransactionType;
import com.freelancehub.payment_service.repository.EscrowRepository;
import com.freelancehub.payment_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentSagaConsumer {

    private final EscrowRepository escrowRepository;
    private final TransactionRepository transactionRepository;
    private final KafkaTemplate<String, SagaEvent> kafkaTemplate;

    @KafkaListener(
            topics = "contract-created",
            groupId = "payment-saga-group"
    )
    @Transactional
    public void onContractCreated(SagaEvent event) {
        log.info("Saga: {} - Auto-funding escrow for contract: {}",
                event.getSagaId(), event.getContractId());

        try {
            if (escrowRepository.findByContractId(event.getContractId()).isPresent()) {
                log.warn("Escrow already exists for contract: {}", event.getContractId());
                return;
            }

            Escrow escrow = Escrow.builder()
                    .contractId(event.getContractId())
                    .clientId(event.getClientId())
                    .clientEmail(event.getClientEmail())
                    .freelancerId(event.getFreelancerId())
                    .freelancerEmail(event.getFreelancerEmail())
                    .totalAmount(event.getAmount())
                    .releasedAmount(BigDecimal.ZERO)
                    .status(EscrowStatus.FUNDED)
                    .build();

            Escrow saved = escrowRepository.save(escrow);

            transactionRepository.save(Transaction.builder()
                    .escrow(saved)
                    .type(TransactionType.ESCROW_FUNDED)
                    .amount(event.getAmount())
                    .description("Auto-funded via Saga - Contract: " + event.getContractId())
                    .build());

            SagaEvent successEvent = SagaEvent.builder()
                    .sagaId(event.getSagaId())
                    .eventType("PAYMENT_FUNDED")
                    .status("SUCCESS")
                    .contractId(event.getContractId())
                    .clientId(event.getClientId())
                    .clientEmail(event.getClientEmail())
                    .freelancerId(event.getFreelancerId())
                    .freelancerEmail(event.getFreelancerEmail())
                    .amount(event.getAmount())
                    .build();

            kafkaTemplate.send("payment-funded", event.getSagaId(), successEvent);
            log.info("Saga: {} - Escrow funded successfully", event.getSagaId());

        } catch (Exception e) {
            log.error("Saga: {} - Payment failed: {}", event.getSagaId(), e.getMessage());

            kafkaTemplate.send("saga-rollback", event.getSagaId(),
                    SagaEvent.builder()
                            .sagaId(event.getSagaId())
                            .eventType("PAYMENT_FAILED")
                            .status("FAILED")
                            .contractId(event.getContractId())
                            .failureReason(e.getMessage())
                            .build());
        }
    }
}