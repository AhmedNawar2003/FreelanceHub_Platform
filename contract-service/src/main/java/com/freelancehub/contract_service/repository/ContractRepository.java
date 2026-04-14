package com.freelancehub.contract_service.repository;

import com.freelancehub.contract_service.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByClientId(Long clientId);
    List<Contract> findByFreelancerId(Long freelancerId);
    Optional<Contract> findByProjectId(Long projectId);
}