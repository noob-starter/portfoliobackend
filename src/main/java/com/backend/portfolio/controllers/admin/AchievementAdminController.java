package com.backend.portfolio.controllers.admin;

import com.backend.portfolio.models.requests.AchievementRequestInsert;
import com.backend.portfolio.models.responses.AchievementResponse;
import com.backend.portfolio.models.updates.AchievementRequestUpdate;
import com.backend.portfolio.services.AchievementService;
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
 * Admin endpoints for Achievement - requires authentication
 * Base URL: /api/v1/admin/achievements
 */
@RestController
@RequestMapping("/api/v1/admin/achievements")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AchievementAdminController {

    private final AchievementService achievementService;

    @GetMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<List<AchievementResponse>> getAllAchievements() {
        log.info("Admin request: Get all achievements");
        List<AchievementResponse> achievements = achievementService.getAllAchievements();
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<AchievementResponse> getAchievementById(@PathVariable Long id) {
        log.info("Admin request: Get achievement by id: {}", id);
        AchievementResponse achievement = achievementService.getAchievementById(id);
        return ResponseEntity.ok(achievement);
    }

    @GetMapping("/profile/{profileId}")
    @RateLimiter(name = "admin")
    public ResponseEntity<List<AchievementResponse>> getAchievementsByProfileId(@PathVariable Long profileId) {
        log.info("Admin request: Get achievements by profile id: {}", profileId);
        List<AchievementResponse> achievements = achievementService.getAllAchievementsByProfileId(profileId);
        return ResponseEntity.ok(achievements);
    }

    @PostMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<AchievementResponse> createAchievement(@Valid @RequestBody AchievementRequestInsert request) {
        log.info("Admin request: Create new achievement");
        AchievementResponse achievement = achievementService.createAchievement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(achievement);
    }

    @PutMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<AchievementResponse> updateAchievement(
            @PathVariable Long id,
            @Valid @RequestBody AchievementRequestUpdate request) {
        log.info("Admin request: Update achievement with id: {}", id);
        AchievementResponse achievement = achievementService.updateAchievement(id, request);
        return ResponseEntity.ok(achievement);
    }

    @PatchMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<AchievementResponse> partialUpdateAchievement(
            @PathVariable Long id,
            @RequestBody AchievementRequestUpdate request) {
        log.info("Admin request: Partial update achievement with id: {}", id);
        AchievementResponse achievement = achievementService.updateAchievement(id, request);
        return ResponseEntity.ok(achievement);
    }

    @DeleteMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) {
        log.info("Admin request: Delete achievement with id: {}", id);
        achievementService.deleteAchievement(id);
        return ResponseEntity.noContent().build();
    }
}

