package com.freelancehub.contract_service.repository;

import com.freelancehub.contract_service.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    List<Milestone> findByContractId(Long contractId);
}