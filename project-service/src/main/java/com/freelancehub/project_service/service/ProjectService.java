package com.freelancehub.project_service.service;


import com.freelancehub.project_service.dto.*;
import com.freelancehub.project_service.entity.Bid;
import com.freelancehub.project_service.entity.Project;
import com.freelancehub.project_service.enums.BidStatus;
import com.freelancehub.project_service.enums.ProjectStatus;
import com.freelancehub.project_service.repository.BidRepository;
import com.freelancehub.project_service.repository.ProjectRepository;
import com.freelancehub.project_service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final BidRepository bidRepository;
    private final JwtService jwtService;

    public ProjectResponse createProject(CreateProjectRequest request, String token) {
        String email = jwtService.extractEmail(token);

        Project project = Project.builder()
                .clientId(extractUserId(token))
                .clientEmail(email)
                .title(request.getTitle())
                .description(request.getDescription())
                .budget(request.getBudget())
                .skills(request.getSkills())
                .build();

        return mapToResponse(projectRepository.save(project), false);
    }

    public List<ProjectResponse> getAllOpenProjects() {
        return projectRepository.findByStatus(ProjectStatus.OPEN)
                .stream().map(p -> mapToResponse(p, false)).toList();
    }

    public ProjectResponse getProjectById(Long id) {
        Project project = findProject(id);
        return mapToResponse(project, true);
    }

    public List<ProjectResponse> getMyProjects(String token) {
        Long clientId = extractUserId(token);
        return projectRepository.findByClientId(clientId)
                .stream().map(p -> mapToResponse(p, false)).toList();
    }

    public ProjectResponse updateProject(Long id, UpdateProjectRequest request, String token) {
        Project project = findProject(id);
        validateOwner(project.getClientEmail(), jwtService.extractEmail(token));

        if (request.getTitle() != null) project.setTitle(request.getTitle());
        if (request.getDescription() != null) project.setDescription(request.getDescription());
        if (request.getBudget() != null) project.setBudget(request.getBudget());
        if (request.getSkills() != null) project.setSkills(request.getSkills());

        return mapToResponse(projectRepository.save(project), false);
    }

    public void deleteProject(Long id, String token) {
        Project project = findProject(id);
        validateOwner(project.getClientEmail(), jwtService.extractEmail(token));
        projectRepository.delete(project);
    }

    public BidResponse placeBid(Long projectId, BidRequest request, String token) {
        Project project = findProject(projectId);
        String email = jwtService.extractEmail(token);

        if (project.getStatus() != ProjectStatus.OPEN)
            throw new RuntimeException("Project is not open for bids");

        if (project.getClientEmail().equals(email))
            throw new RuntimeException("You cannot bid on your own project");

        if (bidRepository.existsByProjectIdAndFreelancerId(projectId, extractUserId(token)))
            throw new RuntimeException("You have already placed a bid on this project");

        Bid bid = Bid.builder()
                .project(project)
                .freelancerId(extractUserId(token))
                .freelancerEmail(email)
                .amount(request.getAmount())
                .proposal(request.getProposal())
                .build();

        return mapBidToResponse(bidRepository.save(bid));
    }

    @Transactional
    public ProjectResponse acceptBid(Long projectId, Long bidId, String token) {
        Project project = findProject(projectId);
        validateOwner(project.getClientEmail(), jwtService.extractEmail(token));

        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found"));

        // Reject all other bids
        bidRepository.findByProjectId(projectId).forEach(b -> {
            b.setStatus(b.getId().equals(bidId) ? BidStatus.ACCEPTED : BidStatus.REJECTED);
            bidRepository.save(b);
        });

        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setAssignedFreelancerId(bid.getFreelancerId());
        projectRepository.save(project);

        return mapToResponse(project, true);
    }

    private Project findProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    private void validateOwner(String ownerEmail, String requestEmail) {
        if (!ownerEmail.equals(requestEmail))
            throw new RuntimeException("You are not authorized to perform this action");
    }

    private Long extractUserId(String token) {
        return jwtService.extractUserId(token);
    }

    private ProjectResponse mapToResponse(Project project, boolean includeBids) {
        List<BidResponse> bids = includeBids ?
                bidRepository.findByProjectId(project.getId())
                        .stream().map(this::mapBidToResponse).toList() : null;

        return ProjectResponse.builder()
                .id(project.getId())
                .clientId(project.getClientId())
                .clientEmail(project.getClientEmail())
                .title(project.getTitle())
                .description(project.getDescription())
                .budget(project.getBudget())
                .skills(project.getSkills())
                .status(project.getStatus())
                .assignedFreelancerId(project.getAssignedFreelancerId())
                .totalBids(bidRepository.findByProjectId(project.getId()).size())
                .createdAt(project.getCreatedAt())
                .bids(bids)
                .build();
    }

    private BidResponse mapBidToResponse(Bid bid) {
        return BidResponse.builder()
                .id(bid.getId())
                .freelancerId(bid.getFreelancerId())
                .freelancerEmail(bid.getFreelancerEmail())
                .amount(bid.getAmount())
                .proposal(bid.getProposal())
                .status(bid.getStatus())
                .createdAt(bid.getCreatedAt())
                .build();
    }
}
