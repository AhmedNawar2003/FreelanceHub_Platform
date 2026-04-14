package com.freelancehub.project_service.controller;



import com.freelancehub.project_service.dto.*;
import com.freelancehub.project_service.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.createProject(request, token.substring(7)));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllOpenProjects() {
        return ResponseEntity.ok(projectService.getAllOpenProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ProjectResponse>> getMyProjects(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(projectService.getMyProjects(token.substring(7)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProjectRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(projectService.updateProject(id, request, token.substring(7)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        projectService.deleteProject(id, token.substring(7));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/bids")
    public ResponseEntity<BidResponse> placeBid(
            @PathVariable Long id,
            @Valid @RequestBody BidRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.placeBid(id, request, token.substring(7)));
    }

    @PutMapping("/{projectId}/bids/{bidId}/accept")
    public ResponseEntity<ProjectResponse> acceptBid(
            @PathVariable Long projectId,
            @PathVariable Long bidId,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(projectService.acceptBid(projectId, bidId, token.substring(7)));
    }
}