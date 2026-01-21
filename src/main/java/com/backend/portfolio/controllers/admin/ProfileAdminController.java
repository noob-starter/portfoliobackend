package com.backend.portfolio.controllers.admin;

import com.backend.portfolio.models.requests.ProfileRequestInsert;
import com.backend.portfolio.models.responses.ProfileResponse;
import com.backend.portfolio.models.updates.ProfileRequestResponse;
import com.backend.portfolio.services.ProfileService;
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
 * Admin endpoints for Profile - requires authentication
 * Base URL: /api/v1/admin/profiles
 */
@RestController
@RequestMapping("/api/v1/admin/profiles")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class ProfileAdminController {

    private final ProfileService profileService;

    @GetMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<List<ProfileResponse>> getAllProfiles() {
        log.info("Admin request: Get all profiles");
        List<ProfileResponse> profiles = profileService.getAllProfiles();
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<ProfileResponse> getProfileById(@PathVariable Long id) {
        log.info("Admin request: Get regular by id: {}", id);
        ProfileResponse profile = profileService.getProfileById(id);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/search")
    @RateLimiter(name = "admin")
    public ResponseEntity<ProfileResponse> getProfileByName(
            @RequestParam String fname,
            @RequestParam String lname) {
        log.info("Admin request: Get profile by name: {} {}", fname, lname);
        ProfileResponse profile = profileService.getProfileByName(fname, lname);
        return ResponseEntity.ok(profile);
    }

    @PostMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<ProfileResponse> createProfile(@Valid @RequestBody ProfileRequestInsert request) {
        log.info("Admin request: Create new regular");
        ProfileResponse profile = profileService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(profile);
    }

    @PutMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody ProfileRequestResponse request) {
        log.info("Admin request: Update regular with id: {}", id);
        ProfileResponse profile = profileService.updateProfile(id, request);
        return ResponseEntity.ok(profile);
    }

    @PatchMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<ProfileResponse> partialUpdateProfile(
            @PathVariable Long id,
            @RequestBody ProfileRequestResponse request) {
        log.info("Admin request: Partial update regular with id: {}", id);
        ProfileResponse profile = profileService.updateProfile(id, request);
        return ResponseEntity.ok(profile);
    }

    @DeleteMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        log.info("Admin request: Delete regular with id: {}", id);
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}

