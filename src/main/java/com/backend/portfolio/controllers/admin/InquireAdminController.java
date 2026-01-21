package com.backend.portfolio.controllers.admin;

import com.backend.portfolio.models.requests.InquireRequestInsert;
import com.backend.portfolio.models.responses.InquireResponse;
import com.backend.portfolio.services.InquireService;
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
 * Admin endpoints for Inquire - requires authentication
 * Base URL: /api/v1/admin/inquires
 */
@RestController
@RequestMapping("/api/v1/admin/inquires")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class InquireAdminController {

    private final InquireService inquireService;

    @GetMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<List<InquireResponse>> getAllInquires() {
        log.info("Admin request: Get all inquires");
        List<InquireResponse> inquires = inquireService.getAllInquires();
        return ResponseEntity.ok(inquires);
    }

    @GetMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<InquireResponse> getInquireById(@PathVariable Long id) {
        log.info("Admin request: Get inquire by id: {}", id);
        InquireResponse inquire = inquireService.getInquireById(id);
        return ResponseEntity.ok(inquire);
    }

    @GetMapping("/profile/{profileId}")
    @RateLimiter(name = "admin")
    public ResponseEntity<List<InquireResponse>> getInquiresByProfileId(@PathVariable Long profileId) {
        log.info("Admin request: Get inquires by profile id: {}", profileId);
        List<InquireResponse> inquires = inquireService.getAllInquiresByProfileId(profileId);
        return ResponseEntity.ok(inquires);
    }

    @PostMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<InquireResponse> createInquire(@Valid @RequestBody InquireRequestInsert request) {
        log.info("Admin request: Create new inquire");
        InquireResponse inquire = inquireService.createInquire(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(inquire);
    }

    @DeleteMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<Void> deleteInquire(@PathVariable Long id) {
        log.info("Admin request: Delete inquire with id: {}", id);
        inquireService.deleteInquire(id);
        return ResponseEntity.noContent().build();
    }
}

