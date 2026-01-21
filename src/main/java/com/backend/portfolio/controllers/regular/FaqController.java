package com.backend.portfolio.controllers.regular;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portfolio.models.responses.FaqResponse;
import com.backend.portfolio.services.FaqService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Public endpoints for FAQ - accessible without authentication
 * Base URL: /api/v1/faqs
 */
@RestController
@RequestMapping("/api/v1/faqs")
@RequiredArgsConstructor
@Slf4j
public class FaqController {

    private final FaqService faqService;

    @GetMapping("/profile/{profileId}")
    @RateLimiter(name = "public")
    public ResponseEntity<List<FaqResponse>> getFaqsByProfileId(@PathVariable Long profileId) {
        log.info("Public request: Get FAQs by profile id: {}", profileId);
        List<FaqResponse> faqs = faqService.getAllFaqsByProfileId(profileId);
        return ResponseEntity.ok(faqs);
    }
}

