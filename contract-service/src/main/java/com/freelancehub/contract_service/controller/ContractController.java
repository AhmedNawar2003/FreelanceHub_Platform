package com.freelancehub.contract_service.controller;

import com.freelancehub.contract_service.dto.*;
import com.freelancehub.contract_service.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    public ResponseEntity<ContractResponse> createContract(
            @Valid @RequestBody CreateContractRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contractService.createContract(request, token.substring(7)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractResponse> getContractById(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.getContractById(id));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ContractResponse> getContractByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(contractService.getContractByProject(projectId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ContractResponse>> getMyContracts(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(contractService.getMyContracts(token.substring(7)));
    }

    @PutMapping("/{contractId}/milestones/{milestoneId}/submit")
    public ResponseEntity<MilestoneResponse> submitMilestone(
            @PathVariable Long contractId,
            @PathVariable Long milestoneId,
            @RequestParam String note,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(contractService.submitMilestone(
                contractId, milestoneId, note, token.substring(7)));
    }

    @PutMapping("/{contractId}/milestones/{milestoneId}/approve")
    public ResponseEntity<MilestoneResponse> approveMilestone(
            @PathVariable Long contractId,
            @PathVariable Long milestoneId,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(contractService.approveMilestone(
                contractId, milestoneId, token.substring(7)));
    }

    @PutMapping("/{contractId}/milestones/{milestoneId}/reject")
    public ResponseEntity<MilestoneResponse> rejectMilestone(
            @PathVariable Long contractId,
            @PathVariable Long milestoneId,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(contractService.rejectMilestone(
                contractId, milestoneId, token.substring(7)));
    }
}
