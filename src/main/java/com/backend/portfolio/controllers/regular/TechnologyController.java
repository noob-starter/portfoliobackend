package com.backend.portfolio.controllers.regular;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portfolio.models.responses.TechnologyResponse;
import com.backend.portfolio.services.TechnologyService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Public endpoints for Technology - accessible without authentication
 * Base URL: /api/v1/technologies
 */
@RestController
@RequestMapping("/api/v1/technologies")
@RequiredArgsConstructor
@Slf4j
public class TechnologyController {

    private final TechnologyService technologyService;

    @GetMapping("/profile/{profileId}")
    @RateLimiter(name = "public")
    public ResponseEntity<List<TechnologyResponse>> getTechnologiesByProfileId(@PathVariable Long profileId) {
        log.info("Public request: Get technologies by profile id: {}", profileId);
        List<TechnologyResponse> technologies = technologyService.getAllTechnologiesByProfileId(profileId);
        return ResponseEntity.ok(technologies);
    }
}

