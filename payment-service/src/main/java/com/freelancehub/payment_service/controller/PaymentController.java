package com.freelancehub.payment_service.controller;

import com.freelancehub.payment_service.dto.*;
import com.freelancehub.payment_service.service.PaymentService;
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
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/escrow")
    public ResponseEntity<PaymentResponse> createEscrow(
            @Valid @RequestBody CreateEscrowRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.createEscrow(request, token.substring(7)));
    }

    @GetMapping("/escrow/{id}")
    public ResponseEntity<PaymentResponse> getEscrowById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getEscrowById(id));
    }

    @GetMapping("/escrow/contract/{contractId}")
    public ResponseEntity<PaymentResponse> getEscrowByContract(
            @PathVariable Long contractId) {
        return ResponseEntity.ok(paymentService.getEscrowByContract(contractId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<PaymentResponse>> getMyPayments(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(paymentService.getMyPayments(token.substring(7)));
    }

    @PutMapping("/escrow/{id}/release-milestone")
    public ResponseEntity<PaymentResponse> releaseMilestonePayment(
            @PathVariable Long id,
            @RequestParam BigDecimal amount,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(paymentService.releaseMilestonePayment(
                id, amount, token.substring(7)));
    }

    @PutMapping("/escrow/{id}/release-full")
    public ResponseEntity<PaymentResponse> releaseFullPayment(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(paymentService.releaseFullPayment(
                id, token.substring(7)));
    }

    @PutMapping("/escrow/{id}/refund")
    public ResponseEntity<PaymentResponse> refundEscrow(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(paymentService.refundEscrow(
                id, token.substring(7)));
    }
}