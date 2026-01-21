package com.backend.portfolio.controllers.regular;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portfolio.models.responses.ProfileResponse;
import com.backend.portfolio.services.ProfileService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Public endpoints for Profile - accessible without authentication
 * Base URL: /api/v1/profiles
 */
@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{id}")
    @RateLimiter(name = "public")
    public ResponseEntity<ProfileResponse> getProfileById(@PathVariable Long id) {
        log.info("Public request: Get regular by id: {}", id);
        ProfileResponse profile = profileService.getProfileById(id);
        return ResponseEntity.ok(profile);
    }
}

