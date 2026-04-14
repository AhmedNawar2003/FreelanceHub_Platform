package com.freelancehub.project_service.repository;


import com.freelancehub.project_service.entity.Project;
import com.freelancehub.project_service.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByClientId(Long clientId);
    List<Project> findByStatus(ProjectStatus status);
    List<Project> findByAssignedFreelancerId(Long freelancerId);
}
