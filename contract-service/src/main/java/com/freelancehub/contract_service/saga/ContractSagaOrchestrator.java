package com.freelancehub.contract_service.saga;

import com.freelancehub.contract_service.config.KafkaConfig;
import com.freelancehub.contract_service.dto.SagaEvent;
import com.freelancehub.contract_service.entity.Contract;
import com.freelancehub.contract_service.enums.ContractStatus;
import com.freelancehub.contract_service.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContractSagaOrchestrator {

    private final KafkaTemplate<String, SagaEvent> kafkaTemplate;
    private final ContractRepository contractRepository;

    public void startSaga(Contract contract) {
        String sagaId = UUID.randomUUID().toString();
        log.info("Starting Saga: {} for contract: {}", sagaId, contract.getId());

        SagaEvent event = SagaEvent.builder()
                .sagaId(sagaId)
                .eventType("CONTRACT_CREATED")
                .status("STARTED")
                .contractId(contract.getId())
                .projectId(contract.getProjectId())
                .clientId(contract.getClientId())
                .clientEmail(contract.getClientEmail())
                .freelancerId(contract.getFreelancerId())
                .freelancerEmail(contract.getFreelancerEmail())
                .amount(contract.getTotalAmount())
                .build();

        kafkaTemplate.send(KafkaConfig.CONTRACT_CREATED_TOPIC, sagaId, event);
        log.info("Saga event published: CONTRACT_CREATED for sagaId: {}", sagaId);
    }

    @KafkaListener(
            topics = KafkaConfig.PAYMENT_FUNDED_TOPIC,
            groupId = "contract-saga-group"
    )
    public void onPaymentFunded(SagaEvent event) {
        log.info("Saga: {} - Payment funded successfully for contract: {}",
                event.getSagaId(), event.getContractId());
    }

    @KafkaListener(
            topics = KafkaConfig.SAGA_ROLLBACK_TOPIC,
            groupId = "contract-saga-group"
    )
    public void onSagaRollback(SagaEvent event) {
        log.warn("Saga: {} - Rolling back contract: {}",
                event.getSagaId(), event.getContractId());

        contractRepository.findById(event.getContractId()).ifPresent(contract -> {
            contract.setStatus(ContractStatus.CANCELLED);
            contractRepository.save(contract);
            log.info("Contract {} cancelled due to saga rollback", contract.getId());
        });
    }
}