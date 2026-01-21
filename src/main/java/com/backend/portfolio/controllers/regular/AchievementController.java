package com.backend.portfolio.controllers.regular;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portfolio.models.responses.AchievementResponse;
import com.backend.portfolio.services.AchievementService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Public endpoints for Achievement - accessible without authentication
 * Base URL: /api/v1/achievements
 */
@RestController
@RequestMapping("/api/v1/achievements")
@RequiredArgsConstructor
@Slf4j
public class AchievementController {

    private final AchievementService achievementService;

    @GetMapping("/profile/{profileId}")
    @RateLimiter(name = "public")
    public ResponseEntity<List<AchievementResponse>> getAchievementsByProfileId(@PathVariable Long profileId) {
        log.info("Public request: Get achievements by profile id: {}", profileId);
        List<AchievementResponse> achievements = achievementService.getAllAchievementsByProfileId(profileId);
        return ResponseEntity.ok(achievements);
    }
}

