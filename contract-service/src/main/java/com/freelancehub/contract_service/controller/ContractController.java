package com.freelancehub.contract_service.controller;

import com.freelancehub.contract_service.dto.*;
import com.freelancehub.contract_service.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
@Tag(name = "Contracts", description = "Contract and milestone management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    @Operation(summary = "Create a contract",
            description = "Creates a contract after bid acceptance (CLIENT only)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contract created"),
            @ApiResponse(responseCode = "400", description = "Contract already exists")
    })
    public ResponseEntity<ContractResponse> createContract(
            @Valid @RequestBody CreateContractRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contractService.createContract(request, token.substring(7)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get contract by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contract found"),
            @ApiResponse(responseCode = "404", description = "Contract not found")
    })
    public ResponseEntity<ContractResponse> getContractById(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.getContractById(id));
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get contract by project ID")
    @ApiResponse(responseCode = "200", description = "Contract found")
    public ResponseEntity<ContractResponse> getContractByProject(
            @PathVariable Long projectId) {
        return ResponseEntity.ok(contractService.getContractByProject(projectId));
    }

    @GetMapping("/my")
    @Operation(summary = "Get my contracts",
            description = "Returns contracts based on role (CLIENT or FREELANCER)")
    @ApiResponse(responseCode = "200", description = "Contracts retrieved")
    public ResponseEntity<List<ContractResponse>> getMyContracts(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(contractService.getMyContracts(token.substring(7)));
    }

    @PutMapping("/{contractId}/milestones/{milestoneId}/submit")
    @Operation(summary = "Submit a milestone",
            description = "Freelancer submits a completed milestone for review")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Milestone submitted"),
            @ApiResponse(responseCode = "403", description = "Not the freelancer")
    })
    public ResponseEntity<MilestoneResponse> submitMilestone(
            @PathVariable Long contractId,
            @PathVariable Long milestoneId,
            @RequestParam String note,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(contractService.submitMilestone(
                contractId, milestoneId, note, token.substring(7)));
    }

    @PutMapping("/{contractId}/milestones/{milestoneId}/approve")
    @Operation(summary = "Approve a milestone",
            description = "Client approves a submitted milestone")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Milestone approved"),
            @ApiResponse(responseCode = "403", description = "Not the client")
    })
    public ResponseEntity<MilestoneResponse> approveMilestone(
            @PathVariable Long contractId,
            @PathVariable Long milestoneId,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(contractService.approveMilestone(
                contractId, milestoneId, token.substring(7)));
    }

    @PutMapping("/{contractId}/milestones/{milestoneId}/reject")
    @Operation(summary = "Reject a milestone",
            description = "Client rejects a submitted milestone")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Milestone rejected"),
            @ApiResponse(responseCode = "403", description = "Not the client")
    })
    public ResponseEntity<MilestoneResponse> rejectMilestone(
            @PathVariable Long contractId,
            @PathVariable Long milestoneId,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(contractService.rejectMilestone(
                contractId, milestoneId, token.substring(7)));
    }
}