package com.backend.portfolio.controllers.admin;

import com.backend.portfolio.models.requests.EducationRequestInsert;
import com.backend.portfolio.models.responses.EducationResponse;
import com.backend.portfolio.models.updates.EducationRequestUpdate;
import com.backend.portfolio.services.EducationService;
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
 * Admin endpoints for Education - requires authentication
 * Base URL: /api/v1/admin/educations
 */
@RestController
@RequestMapping("/api/v1/admin/educations")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class EducationAdminController {

    private final EducationService educationService;

    @GetMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<List<EducationResponse>> getAllEducations() {
        log.info("Admin request: Get all educations");
        List<EducationResponse> educations = educationService.getAllEducations();
        return ResponseEntity.ok(educations);
    }

    @GetMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<EducationResponse> getEducationById(@PathVariable Long id) {
        log.info("Admin request: Get education by id: {}", id);
        EducationResponse education = educationService.getEducationById(id);
        return ResponseEntity.ok(education);
    }

    @GetMapping("/profile/{profileId}")
    @RateLimiter(name = "admin")
    public ResponseEntity<List<EducationResponse>> getEducationsByProfileId(@PathVariable Long profileId) {
        log.info("Admin request: Get educations by profile id: {}", profileId);
        List<EducationResponse> educations = educationService.getAllEducationsByProfileId(profileId);
        return ResponseEntity.ok(educations);
    }

    @PostMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<EducationResponse> createEducation(@Valid @RequestBody EducationRequestInsert request) {
        log.info("Admin request: Create new education");
        EducationResponse education = educationService.createEducation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(education);
    }

    @PutMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<EducationResponse> updateEducation(
            @PathVariable Long id,
            @Valid @RequestBody EducationRequestUpdate request) {
        log.info("Admin request: Update education with id: {}", id);
        EducationResponse education = educationService.updateEducation(id, request);
        return ResponseEntity.ok(education);
    }

    @PatchMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<EducationResponse> partialUpdateEducation(
            @PathVariable Long id,
            @RequestBody EducationRequestUpdate request) {
        log.info("Admin request: Partial update education with id: {}", id);
        EducationResponse education = educationService.updateEducation(id, request);
        return ResponseEntity.ok(education);
    }

    @DeleteMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long id) {
        log.info("Admin request: Delete education with id: {}", id);
        educationService.deleteEducation(id);
        return ResponseEntity.noContent().build();
    }
}

