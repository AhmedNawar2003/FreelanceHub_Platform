package com.freelancehub.payment_service.repository;

import com.freelancehub.payment_service.entity.Escrow;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EscrowRepository extends JpaRepository<Escrow, Long> {
    Optional<Escrow> findByContractId(Long contractId);
    List<Escrow> findByClientId(Long clientId);
    List<Escrow> findByFreelancerId(Long freelancerId);
}
