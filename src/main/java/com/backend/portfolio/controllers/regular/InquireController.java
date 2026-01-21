package com.backend.portfolio.controllers.regular;

import com.backend.portfolio.models.requests.InquireRequestInsert;
import com.backend.portfolio.models.responses.InquireResponse;
import com.backend.portfolio.services.InquireService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public endpoints for Inquire - accessible without authentication
 * Base URL: /api/v1/inquires
 */
@RestController
@RequestMapping("/api/v1/inquires")
@RequiredArgsConstructor
@Slf4j
public class InquireController {

    private final InquireService inquireService;

    @PostMapping
    @RateLimiter(name = "public")
    public ResponseEntity<InquireResponse> createInquire(@Valid @RequestBody InquireRequestInsert request) {
        log.info("Public request: Create new inquire");
        InquireResponse inquire = inquireService.createInquire(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(inquire);
    }
}

