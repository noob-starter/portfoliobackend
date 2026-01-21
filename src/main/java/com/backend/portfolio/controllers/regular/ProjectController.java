package com.backend.portfolio.controllers.regular;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portfolio.models.responses.ProjectResponse;
import com.backend.portfolio.services.ProjectService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Public endpoints for Project - accessible without authentication
 * Base URL: /api/v1/projects
 */
@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/profile/{profileId}")
    @RateLimiter(name = "public")
    public ResponseEntity<List<ProjectResponse>> getProjectsByProfileId(@PathVariable Long profileId) {
        log.info("Public request: Get projects by profile id: {}", profileId);
        List<ProjectResponse> projects = projectService.getAllProjectsByProfileId(profileId);
        return ResponseEntity.ok(projects);
    }
}

