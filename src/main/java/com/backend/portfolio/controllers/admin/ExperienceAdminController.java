package com.backend.portfolio.controllers.admin;

import com.backend.portfolio.models.requests.ExperienceRequestInsert;
import com.backend.portfolio.models.responses.ExperienceResponse;
import com.backend.portfolio.models.updates.ExperienceRequestUpdate;
import com.backend.portfolio.services.ExperienceService;
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
 * Admin endpoints for Experience - requires authentication
 * Base URL: /api/v1/admin/experiences
 */
@RestController
@RequestMapping("/api/v1/admin/experiences")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class ExperienceAdminController {

    private final ExperienceService experienceService;

    @GetMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<List<ExperienceResponse>> getAllExperiences() {
        log.info("Admin request: Get all experiences");
        List<ExperienceResponse> experiences = experienceService.getAllExperiences();
        return ResponseEntity.ok(experiences);
    }

    @GetMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<ExperienceResponse> getExperienceById(@PathVariable Long id) {
        log.info("Admin request: Get experience by id: {}", id);
        ExperienceResponse experience = experienceService.getExperienceById(id);
        return ResponseEntity.ok(experience);
    }

    @GetMapping("/profile/{profileId}")
    @RateLimiter(name = "admin")
    public ResponseEntity<List<ExperienceResponse>> getExperiencesByProfileId(@PathVariable Long profileId) {
        log.info("Admin request: Get experiences by profile id: {}", profileId);
        List<ExperienceResponse> experiences = experienceService.getAllExperiencesByProfileId(profileId);
        return ResponseEntity.ok(experiences);
    }

    @PostMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<ExperienceResponse> createExperience(@Valid @RequestBody ExperienceRequestInsert request) {
        log.info("Admin request: Create new experience");
        ExperienceResponse experience = experienceService.createExperience(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(experience);
    }

    @PutMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<ExperienceResponse> updateExperience(
            @PathVariable Long id,
            @Valid @RequestBody ExperienceRequestUpdate request) {
        log.info("Admin request: Update experience with id: {}", id);
        ExperienceResponse experience = experienceService.updateExperience(id, request);
        return ResponseEntity.ok(experience);
    }

    @PatchMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<ExperienceResponse> partialUpdateExperience(
            @PathVariable Long id,
            @RequestBody ExperienceRequestUpdate request) {
        log.info("Admin request: Partial update experience with id: {}", id);
        ExperienceResponse experience = experienceService.updateExperience(id, request);
        return ResponseEntity.ok(experience);
    }

    @DeleteMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        log.info("Admin request: Delete experience with id: {}", id);
        experienceService.deleteExperience(id);
        return ResponseEntity.noContent().build();
    }
}

