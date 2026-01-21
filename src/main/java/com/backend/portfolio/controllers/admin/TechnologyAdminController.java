package com.backend.portfolio.controllers.admin;

import com.backend.portfolio.models.requests.TechnologyRequestInsert;
import com.backend.portfolio.models.responses.TechnologyResponse;
import com.backend.portfolio.models.updates.TechnologyRequestUpdate;
import com.backend.portfolio.services.TechnologyService;
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
 * Admin endpoints for Technology - requires authentication
 * Base URL: /api/v1/admin/technologies
 */
@RestController
@RequestMapping("/api/v1/admin/technologies")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class TechnologyAdminController {

    private final TechnologyService technologyService;

    @GetMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<List<TechnologyResponse>> getAllTechnologies() {
        log.info("Admin request: Get all technologies");
        List<TechnologyResponse> technologies = technologyService.getAllTechnologies();
        return ResponseEntity.ok(technologies);
    }

    @GetMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<TechnologyResponse> getTechnologyById(@PathVariable Long id) {
        log.info("Admin request: Get technology by id: {}", id);
        TechnologyResponse technology = technologyService.getTechnologyById(id);
        return ResponseEntity.ok(technology);
    }

    @GetMapping("/search")
    @RateLimiter(name = "admin")
    public ResponseEntity<TechnologyResponse> getTechnologyByName(@RequestParam String name) {
        log.info("Admin request: Get technology by name: {}", name);
        TechnologyResponse technology = technologyService.getTechnologyByName(name);
        return ResponseEntity.ok(technology);
    }

    @GetMapping("/category/{category}")
    @RateLimiter(name = "admin")
    public ResponseEntity<List<TechnologyResponse>> getTechnologiesByCategory(@PathVariable String category) {
        log.info("Admin request: Get technologies by category: {}", category);
        List<TechnologyResponse> technologies = technologyService.getAllTechnologiesByCategory(category);
        return ResponseEntity.ok(technologies);
    }

    @PostMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<TechnologyResponse> createTechnology(@Valid @RequestBody TechnologyRequestInsert request) {
        log.info("Admin request: Create new technology");
        TechnologyResponse technology = technologyService.createTechnology(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(technology);
    }

    @PutMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<TechnologyResponse> updateTechnology(
            @PathVariable Long id,
            @Valid @RequestBody TechnologyRequestUpdate request) {
        log.info("Admin request: Update technology with id: {}", id);
        TechnologyResponse technology = technologyService.updateTechnology(id, request);
        return ResponseEntity.ok(technology);
    }

    @PatchMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<TechnologyResponse> partialUpdateTechnology(
            @PathVariable Long id,
            @RequestBody TechnologyRequestUpdate request) {
        log.info("Admin request: Partial update technology with id: {}", id);
        TechnologyResponse technology = technologyService.updateTechnology(id, request);
        return ResponseEntity.ok(technology);
    }

    @DeleteMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<Void> deleteTechnology(@PathVariable Long id) {
        log.info("Admin request: Delete technology with id: {}", id);
        technologyService.deleteTechnology(id);
        return ResponseEntity.noContent().build();
    }
}

