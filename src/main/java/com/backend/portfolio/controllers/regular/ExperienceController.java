package com.backend.portfolio.controllers.regular;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portfolio.models.responses.ExperienceResponse;
import com.backend.portfolio.services.ExperienceService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Public endpoints for Experience - accessible without authentication
 * Base URL: /api/v1/experiences
 */
@RestController
@RequestMapping("/api/v1/experiences")
@RequiredArgsConstructor
@Slf4j
public class ExperienceController {

    private final ExperienceService experienceService;

    @GetMapping("/profile/{profileId}")
    @RateLimiter(name = "public")
    public ResponseEntity<List<ExperienceResponse>> getExperiencesByProfileId(@PathVariable Long profileId) {
        log.info("Public request: Get experiences by profile id: {}", profileId);
        List<ExperienceResponse> experiences = experienceService.getAllExperiencesByProfileId(profileId);
        return ResponseEntity.ok(experiences);
    }
}

