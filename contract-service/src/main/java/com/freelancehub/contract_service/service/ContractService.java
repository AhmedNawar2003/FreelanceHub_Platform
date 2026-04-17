package com.freelancehub.contract_service.service;


import com.freelancehub.contract_service.dto.*;
import com.freelancehub.contract_service.entity.Contract;
import com.freelancehub.contract_service.entity.Milestone;
import com.freelancehub.contract_service.enums.ContractStatus;
import com.freelancehub.contract_service.enums.MilestoneStatus;
import com.freelancehub.contract_service.repository.ContractRepository;
import com.freelancehub.contract_service.repository.MilestoneRepository;
import com.freelancehub.contract_service.saga.ContractSagaOrchestrator;
import com.freelancehub.contract_service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final MilestoneRepository milestoneRepository;
    private final JwtService jwtService;
    private final ContractSagaOrchestrator sagaOrchestrator;

    @Transactional
    public ContractResponse createContract(CreateContractRequest request, String token) {
        String clientEmail = jwtService.extractEmail(token);

        if (contractRepository.findByProjectId(request.getProjectId()).isPresent())
            throw new RuntimeException("Contract already exists for this project");

        Contract contract = Contract.builder()
                .projectId(request.getProjectId())
                .clientId(jwtService.extractUserId(token))
                .clientEmail(clientEmail)
                .freelancerId(request.getFreelancerId())
                .freelancerEmail(request.getFreelancerEmail())
                .totalAmount(request.getTotalAmount())
                .build();

        Contract saved = contractRepository.save(contract);

        if (request.getMilestones() != null) {
            request.getMilestones().forEach(m -> {
                milestoneRepository.save(Milestone.builder()
                        .contract(saved)
                        .title(m.getTitle())
                        .description(m.getDescription())
                        .amount(m.getAmount())
                        .build());
            });
        }

        // Start Saga
        sagaOrchestrator.startSaga(saved);

        return mapToResponse(saved);
    }
    public ContractResponse getContractById(Long id) {
        return mapToResponse(findContract(id));
    }

    public ContractResponse getContractByProject(Long projectId) {
        Contract contract = contractRepository.findByProjectId(projectId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
        return mapToResponse(contract);
    }

    public List<ContractResponse> getMyContracts(String token) {
        String email = jwtService.extractEmail(token);
        String role = jwtService.extractRole(token);

        List<Contract> contracts = role.equals("CLIENT")
                ? contractRepository.findByClientId(extractUserId(token))
                : contractRepository.findByFreelancerId(extractUserId(token));

        return contracts.stream().map(this::mapToResponse).toList();
    }

    public MilestoneResponse submitMilestone(Long contractId, Long milestoneId,
                                             String note, String token) {
        Contract contract = findContract(contractId);
        validateFreelancer(contract.getFreelancerEmail(), jwtService.extractEmail(token));

        Milestone milestone = findMilestone(milestoneId);

        if (milestone.getStatus() != MilestoneStatus.PENDING &&
                milestone.getStatus() != MilestoneStatus.IN_PROGRESS)
            throw new RuntimeException("Milestone cannot be submitted in current status");

        milestone.setStatus(MilestoneStatus.SUBMITTED);
        milestone.setSubmissionNote(note);
        return mapMilestoneToResponse(milestoneRepository.save(milestone));
    }

    @Transactional
    public MilestoneResponse approveMilestone(Long contractId, Long milestoneId, String token) {
        Contract contract = findContract(contractId);
        validateClient(contract.getClientEmail(), jwtService.extractEmail(token));

        Milestone milestone = findMilestone(milestoneId);

        if (milestone.getStatus() != MilestoneStatus.SUBMITTED)
            throw new RuntimeException("Milestone is not submitted yet");

        milestone.setStatus(MilestoneStatus.APPROVED);
        milestoneRepository.save(milestone);

        // Check if all milestones are approved
        List<Milestone> all = milestoneRepository.findByContractId(contractId);
        boolean allDone = all.stream()
                .allMatch(m -> m.getStatus() == MilestoneStatus.APPROVED);

        if (allDone) {
            contract.setStatus(ContractStatus.COMPLETED);
            contractRepository.save(contract);
        }

        return mapMilestoneToResponse(milestone);
    }

    public MilestoneResponse rejectMilestone(Long contractId, Long milestoneId, String token) {
        Contract contract = findContract(contractId);
        validateClient(contract.getClientEmail(), jwtService.extractEmail(token));

        Milestone milestone = findMilestone(milestoneId);
        milestone.setStatus(MilestoneStatus.REJECTED);
        return mapMilestoneToResponse(milestoneRepository.save(milestone));
    }

    private Contract findContract(Long id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
    }

    private Milestone findMilestone(Long id) {
        return milestoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Milestone not found"));
    }

    private void validateClient(String ownerEmail, String requestEmail) {
        if (!ownerEmail.equals(requestEmail))
            throw new RuntimeException("Only the client can perform this action");
    }

    private void validateFreelancer(String ownerEmail, String requestEmail) {
        if (!ownerEmail.equals(requestEmail))
            throw new RuntimeException("Only the freelancer can perform this action");
    }

    private Long extractUserId(String token) {
        return jwtService.extractUserId(token);
    }

    private ContractResponse mapToResponse(Contract contract) {
        List<MilestoneResponse> milestones = milestoneRepository
                .findByContractId(contract.getId())
                .stream().map(this::mapMilestoneToResponse).toList();

        return ContractResponse.builder()
                .id(contract.getId())
                .projectId(contract.getProjectId())
                .clientId(contract.getClientId())
                .clientEmail(contract.getClientEmail())
                .freelancerId(contract.getFreelancerId())
                .freelancerEmail(contract.getFreelancerEmail())
                .totalAmount(contract.getTotalAmount())
                .status(contract.getStatus())
                .createdAt(contract.getCreatedAt())
                .milestones(milestones)
                .build();
    }

    private MilestoneResponse mapMilestoneToResponse(Milestone milestone) {
        return MilestoneResponse.builder()
                .id(milestone.getId())
                .title(milestone.getTitle())
                .description(milestone.getDescription())
                .amount(milestone.getAmount())
                .status(milestone.getStatus())
                .submissionNote(milestone.getSubmissionNote())
                .createdAt(milestone.getCreatedAt())
                .build();
    }
}