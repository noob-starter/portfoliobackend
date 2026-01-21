package com.backend.portfolio.controllers.admin;

import com.backend.portfolio.models.requests.ProjectRequestInsert;
import com.backend.portfolio.models.responses.ProjectResponse;
import com.backend.portfolio.models.updates.ProjectRequestUpdate;
import com.backend.portfolio.services.ProjectService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin endpoints for Project - requires authentication
 * Base URL: /api/v1/admin/projects
 */
@RestController
@RequestMapping("/api/v1/admin/projects")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class ProjectAdminController {

    private final ProjectService projectService;

    @GetMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        log.info("Admin request: Get all projects");
        List<ProjectResponse> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        log.info("Admin request: Get project by id: {}", id);
        ProjectResponse project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/profile/{profileId}")
    @RateLimiter(name = "admin")
    public ResponseEntity<List<ProjectResponse>> getProjectsByProfileId(@PathVariable Long profileId) {
        log.info("Admin request: Get projects by profile id: {}", profileId);
        List<ProjectResponse> projects = projectService.getAllProjectsByProfileId(profileId);
        return ResponseEntity.ok(projects);
    }

    @PostMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequestInsert request) {
        log.info("Admin request: Create new project");
        ProjectResponse project = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    @PutMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequestUpdate request) {
        log.info("Admin request: Update project with id: {}", id);
        ProjectResponse project = projectService.updateProject(id, request);
        return ResponseEntity.ok(project);
    }

    @PatchMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<ProjectResponse> partialUpdateProject(
            @PathVariable Long id,
            @RequestBody ProjectRequestUpdate request) {
        log.info("Admin request: Partial update project with id: {}", id);
        ProjectResponse project = projectService.updateProject(id, request);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.info("Admin request: Delete project with id: {}", id);
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}

