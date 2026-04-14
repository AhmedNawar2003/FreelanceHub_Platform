package com.freelancehub.project_service.dto;


import com.freelancehub.project_service.enums.ProjectStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ProjectResponse {
    private Long id;
    private Long clientId;
    private String clientEmail;
    private String title;
    private String description;
    private BigDecimal budget;
    private String skills;
    private ProjectStatus status;
    private Long assignedFreelancerId;
    private int totalBids;
    private LocalDateTime createdAt;
    private List<BidResponse> bids;
}
