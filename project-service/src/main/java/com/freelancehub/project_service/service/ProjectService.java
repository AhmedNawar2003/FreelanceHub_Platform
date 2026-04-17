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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final BidRepository bidRepository;
    private final JwtService jwtService;

    @CacheEvict(value = {"projects", "open-projects"}, allEntries = true)
    public ProjectResponse createProject(CreateProjectRequest request, String token) {
        String email = jwtService.extractEmail(token);
        Project project = Project.builder()
                .clientId(jwtService.extractUserId(token))
                .clientEmail(email)
                .title(request.getTitle())
                .description(request.getDescription())
                .budget(request.getBudget())
                .skills(request.getSkills())
                .build();
        return mapToResponse(projectRepository.save(project), false);
    }

    @Cacheable(value = "open-projects", key = "'all'")
    public List<ProjectResponse> getAllOpenProjects() {
        return projectRepository.findByStatus(ProjectStatus.OPEN)
                .stream().map(p -> mapToResponse(p, false)).toList();
    }

    @Cacheable(value = "projects", key = "#id")
    public ProjectResponse getProjectById(Long id) {
        return mapToResponse(findProject(id), true);
    }

    public List<ProjectResponse> getMyProjects(String token) {
        return projectRepository.findByClientId(jwtService.extractUserId(token))
                .stream().map(p -> mapToResponse(p, false)).toList();
    }

    @CacheEvict(value = {"projects", "open-projects"}, allEntries = true)
    public ProjectResponse updateProject(Long id, UpdateProjectRequest request, String token) {
        Project project = findProject(id);
        validateOwner(project.getClientEmail(), jwtService.extractEmail(token));
        if (request.getTitle() != null) project.setTitle(request.getTitle());
        if (request.getDescription() != null) project.setDescription(request.getDescription());
        if (request.getBudget() != null) project.setBudget(request.getBudget());
        if (request.getSkills() != null) project.setSkills(request.getSkills());
        return mapToResponse(projectRepository.save(project), false);
    }

    @CacheEvict(value = {"projects", "open-projects"}, allEntries = true)
    public void deleteProject(Long id, String token) {
        Project project = findProject(id);
        validateOwner(project.getClientEmail(), jwtService.extractEmail(token));
        projectRepository.delete(project);
    }

    @CacheEvict(value = {"projects", "open-projects"}, allEntries = true)
    public BidResponse placeBid(Long projectId, BidRequest request, String token) {
        Project project = findProject(projectId);
        String email = jwtService.extractEmail(token);
        Long userId = jwtService.extractUserId(token);

        if (project.getStatus() != ProjectStatus.OPEN)
            throw new RuntimeException("Project is not open for bids");
        if (project.getClientEmail().equals(email))
            throw new RuntimeException("You cannot bid on your own project");
        if (bidRepository.existsByProjectIdAndFreelancerId(projectId, userId))
            throw new RuntimeException("You have already placed a bid");

        Bid bid = Bid.builder()
                .project(project)
                .freelancerId(userId)
                .freelancerEmail(email)
                .amount(request.getAmount())
                .proposal(request.getProposal())
                .build();
        return mapBidToResponse(bidRepository.save(bid));
    }

    @CacheEvict(value = {"projects", "open-projects"}, allEntries = true)
    @Transactional
    public ProjectResponse acceptBid(Long projectId, Long bidId, String token) {
        Project project = findProject(projectId);
        validateOwner(project.getClientEmail(), jwtService.extractEmail(token));

        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found"));

        bidRepository.findByProjectId(projectId).forEach(b -> {
            b.setStatus(b.getId().equals(bidId) ? BidStatus.ACCEPTED : BidStatus.REJECTED);
            bidRepository.save(b);
        });

        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setAssignedFreelancerId(bid.getFreelancerId());
        return mapToResponse(projectRepository.save(project), true);
    }

    private Project findProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    private void validateOwner(String ownerEmail, String requestEmail) {
        if (!ownerEmail.equals(requestEmail))
            throw new RuntimeException("Not authorized");
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
