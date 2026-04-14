package com.freelancehub.payment_service.service;

import com.freelancehub.payment_service.dto.*;
import com.freelancehub.payment_service.entity.Escrow;
import com.freelancehub.payment_service.entity.Transaction;
import com.freelancehub.payment_service.enums.EscrowStatus;
import com.freelancehub.payment_service.enums.TransactionType;
import com.freelancehub.payment_service.repository.EscrowRepository;
import com.freelancehub.payment_service.repository.TransactionRepository;
import com.freelancehub.payment_service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final EscrowRepository escrowRepository;
    private final TransactionRepository transactionRepository;
    private final JwtService jwtService;

    @Transactional
    public PaymentResponse createEscrow(CreateEscrowRequest request, String token) {
        String clientEmail = jwtService.extractEmail(token);

        if (escrowRepository.findByContractId(request.getContractId()).isPresent())
            throw new RuntimeException("Escrow already exists for this contract");

        Escrow escrow = Escrow.builder()
                .contractId(request.getContractId())
                .clientId(extractUserId(token))
                .clientEmail(clientEmail)
                .freelancerId(request.getFreelancerId())
                .freelancerEmail(request.getFreelancerEmail())
                .totalAmount(request.getTotalAmount())
                .releasedAmount(BigDecimal.ZERO)
                .status(EscrowStatus.FUNDED)
                .build();

        Escrow saved = escrowRepository.save(escrow);

        createTransaction(saved, TransactionType.ESCROW_FUNDED,
                request.getTotalAmount(), "Escrow funded by client");

        return mapToResponse(saved);
    }

    @Transactional
    public PaymentResponse releaseMilestonePayment(Long escrowId,
                                                   BigDecimal amount,
                                                   String token) {
        Escrow escrow = findEscrow(escrowId);
        validateClient(escrow.getClientEmail(), jwtService.extractEmail(token));

        if (escrow.getStatus() != EscrowStatus.FUNDED)
            throw new RuntimeException("Escrow is not in funded status");

        BigDecimal remaining = escrow.getTotalAmount()
                .subtract(escrow.getReleasedAmount());

        if (amount.compareTo(remaining) > 0)
            throw new RuntimeException("Amount exceeds remaining escrow balance");

        escrow.setReleasedAmount(escrow.getReleasedAmount().add(amount));

        if (escrow.getReleasedAmount().compareTo(escrow.getTotalAmount()) == 0) {
            escrow.setStatus(EscrowStatus.RELEASED);
        }

        escrowRepository.save(escrow);

        createTransaction(escrow, TransactionType.MILESTONE_RELEASED,
                amount, "Milestone payment released to freelancer");

        return mapToResponse(escrow);
    }

    @Transactional
    public PaymentResponse releaseFullPayment(Long escrowId, String token) {
        Escrow escrow = findEscrow(escrowId);
        validateClient(escrow.getClientEmail(), jwtService.extractEmail(token));

        if (escrow.getStatus() != EscrowStatus.FUNDED)
            throw new RuntimeException("Escrow is not in funded status");

        BigDecimal remaining = escrow.getTotalAmount()
                .subtract(escrow.getReleasedAmount());

        escrow.setReleasedAmount(escrow.getTotalAmount());
        escrow.setStatus(EscrowStatus.RELEASED);
        escrowRepository.save(escrow);

        createTransaction(escrow, TransactionType.FULL_RELEASE,
                remaining, "Full payment released to freelancer");

        return mapToResponse(escrow);
    }

    @Transactional
    public PaymentResponse refundEscrow(Long escrowId, String token) {
        Escrow escrow = findEscrow(escrowId);
        validateClient(escrow.getClientEmail(), jwtService.extractEmail(token));

        if (escrow.getStatus() != EscrowStatus.FUNDED)
            throw new RuntimeException("Escrow cannot be refunded");

        BigDecimal refundAmount = escrow.getTotalAmount()
                .subtract(escrow.getReleasedAmount());

        escrow.setStatus(EscrowStatus.REFUNDED);
        escrowRepository.save(escrow);

        createTransaction(escrow, TransactionType.REFUND,
                refundAmount, "Escrow refunded to client");

        return mapToResponse(escrow);
    }

    public PaymentResponse getEscrowById(Long id) {
        return mapToResponse(findEscrow(id));
    }

    public PaymentResponse getEscrowByContract(Long contractId) {
        Escrow escrow = escrowRepository.findByContractId(contractId)
                .orElseThrow(() -> new RuntimeException("Escrow not found"));
        return mapToResponse(escrow);
    }

    public List<PaymentResponse> getMyPayments(String token) {
        String role = jwtService.extractRole(token);
        Long userId = extractUserId(token);

        List<Escrow> escrows = role.equals("CLIENT")
                ? escrowRepository.findByClientId(userId)
                : escrowRepository.findByFreelancerId(userId);

        return escrows.stream().map(this::mapToResponse).toList();
    }

    private void createTransaction(Escrow escrow, TransactionType type,
                                   BigDecimal amount, String description) {
        Transaction transaction = Transaction.builder()
                .escrow(escrow)
                .type(type)
                .amount(amount)
                .description(description)
                .build();
        transactionRepository.save(transaction);
    }

    private Escrow findEscrow(Long id) {
        return escrowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Escrow not found"));
    }

    private void validateClient(String ownerEmail, String requestEmail) {
        if (!ownerEmail.equals(requestEmail))
            throw new RuntimeException("Only the client can perform this action");
    }

    private Long extractUserId(String token) {
        return jwtService.extractUserId(token);
    }

    private PaymentResponse mapToResponse(Escrow escrow) {
        List<TransactionResponse> transactions = transactionRepository
                .findByEscrowId(escrow.getId())
                .stream().map(this::mapTransactionToResponse).toList();

        return PaymentResponse.builder()
                .id(escrow.getId())
                .contractId(escrow.getContractId())
                .clientId(escrow.getClientId())
                .clientEmail(escrow.getClientEmail())
                .freelancerId(escrow.getFreelancerId())
                .freelancerEmail(escrow.getFreelancerEmail())
                .totalAmount(escrow.getTotalAmount())
                .releasedAmount(escrow.getReleasedAmount())
                .remainingAmount(escrow.getTotalAmount()
                        .subtract(escrow.getReleasedAmount()))
                .status(escrow.getStatus())
                .createdAt(escrow.getCreatedAt())
                .transactions(transactions)
                .build();
    }

    private TransactionResponse mapTransactionToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}