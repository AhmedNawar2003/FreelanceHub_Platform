package com.freelancehub.payment_service.controller;

import com.freelancehub.payment_service.dto.*;
import com.freelancehub.payment_service.service.PaymentService;
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

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Escrow payment management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/escrow")
    @Operation(summary = "Create escrow",
            description = "Client funds an escrow for a contract")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Escrow created and funded"),
            @ApiResponse(responseCode = "400", description = "Escrow already exists")
    })
    public ResponseEntity<PaymentResponse> createEscrow(
            @Valid @RequestBody CreateEscrowRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.createEscrow(request, token.substring(7)));
    }

    @GetMapping("/escrow/{id}")
    @Operation(summary = "Get escrow by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Escrow found"),
            @ApiResponse(responseCode = "404", description = "Escrow not found")
    })
    public ResponseEntity<PaymentResponse> getEscrowById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getEscrowById(id));
    }

    @GetMapping("/escrow/contract/{contractId}")
    @Operation(summary = "Get escrow by contract ID")
    @ApiResponse(responseCode = "200", description = "Escrow found")
    public ResponseEntity<PaymentResponse> getEscrowByContract(
            @PathVariable Long contractId) {
        return ResponseEntity.ok(paymentService.getEscrowByContract(contractId));
    }

    @GetMapping("/my")
    @Operation(summary = "Get my payments",
            description = "Returns payments based on role (CLIENT or FREELANCER)")
    @ApiResponse(responseCode = "200", description = "Payments retrieved")
    public ResponseEntity<List<PaymentResponse>> getMyPayments(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(paymentService.getMyPayments(token.substring(7)));
    }

    @PutMapping("/escrow/{id}/release-milestone")
    @Operation(summary = "Release milestone payment",
            description = "Client releases a partial payment for an approved milestone")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment released"),
            @ApiResponse(responseCode = "400", description = "Amount exceeds balance")
    })
    public ResponseEntity<PaymentResponse> releaseMilestonePayment(
            @PathVariable Long id,
            @RequestParam BigDecimal amount,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(paymentService.releaseMilestonePayment(
                id, amount, token.substring(7)));
    }

    @PutMapping("/escrow/{id}/release-full")
    @Operation(summary = "Release full payment",
            description = "Client releases the full remaining escrow amount")
    @ApiResponse(responseCode = "200", description = "Full payment released")
    public ResponseEntity<PaymentResponse> releaseFullPayment(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(paymentService.releaseFullPayment(
                id, token.substring(7)));
    }

    @PutMapping("/escrow/{id}/refund")
    @Operation(summary = "Refund escrow",
            description = "Client requests a refund of the remaining escrow amount")
    @ApiResponse(responseCode = "200", description = "Escrow refunded")
    public ResponseEntity<PaymentResponse> refundEscrow(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(paymentService.refundEscrow(
                id, token.substring(7)));
    }
}