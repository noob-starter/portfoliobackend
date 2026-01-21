package com.backend.portfolio.controllers.regular;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portfolio.models.responses.EducationResponse;
import com.backend.portfolio.services.EducationService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Public endpoints for Education - accessible without authentication
 * Base URL: /api/v1/educations
 */
@RestController
@RequestMapping("/api/v1/educations")
@RequiredArgsConstructor
@Slf4j
public class EducationController {

    private final EducationService educationService;

    @GetMapping("/profile/{profileId}")
    @RateLimiter(name = "public")
    public ResponseEntity<List<EducationResponse>> getEducationsByProfileId(@PathVariable Long profileId) {
        log.info("Public request: Get educations by profile id: {}", profileId);
        List<EducationResponse> educations = educationService.getAllEducationsByProfileId(profileId);
        return ResponseEntity.ok(educations);
    }
}

