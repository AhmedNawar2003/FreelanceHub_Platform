package com.freelancehub.project_service.controller;

import com.freelancehub.project_service.dto.BidResponse;
import com.freelancehub.project_service.dto.*;
import com.freelancehub.project_service.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Project management and bidding endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @Operation(summary = "Create a project",
            description = "Creates a new project (CLIENT role only)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Project created"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.createProject(request, token.substring(7)));
    }

    @GetMapping
    @Operation(summary = "Get all open projects",
            description = "Returns all projects with OPEN status")
    @ApiResponse(responseCode = "200", description = "Projects retrieved")
    public ResponseEntity<List<ProjectResponse>> getAllOpenProjects() {
        return ResponseEntity.ok(projectService.getAllOpenProjects());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID",
            description = "Returns a project with all its bids")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project found"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @GetMapping("/my")
    @Operation(summary = "Get my projects",
            description = "Returns all projects created by the authenticated client")
    @ApiResponse(responseCode = "200", description = "Projects retrieved")
    public ResponseEntity<List<ProjectResponse>> getMyProjects(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(projectService.getMyProjects(token.substring(7)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project",
            description = "Updates project details (owner only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project updated"),
            @ApiResponse(responseCode = "403", description = "Not the project owner")
    })
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProjectRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(
                projectService.updateProject(id, request, token.substring(7)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project",
            description = "Deletes a project (owner only)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Project deleted"),
            @ApiResponse(responseCode = "403", description = "Not the project owner")
    })
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        projectService.deleteProject(id, token.substring(7));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/bids")
    @Operation(summary = "Place a bid",
            description = "Places a bid on a project (FREELANCER role only)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Bid placed"),
            @ApiResponse(responseCode = "400", description = "Already bid or project not open")
    })
    public ResponseEntity<BidResponse> placeBid(
            @PathVariable Long id,
            @Valid @RequestBody BidRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.placeBid(id, request, token.substring(7)));
    }

    @PutMapping("/{projectId}/bids/{bidId}/accept")
    @Operation(summary = "Accept a bid",
            description = "Accepts a freelancer's bid (project owner only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bid accepted"),
            @ApiResponse(responseCode = "403", description = "Not the project owner")
    })
    public ResponseEntity<ProjectResponse> acceptBid(
            @PathVariable Long projectId,
            @PathVariable Long bidId,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(
                projectService.acceptBid(projectId, bidId, token.substring(7)));
    }
}