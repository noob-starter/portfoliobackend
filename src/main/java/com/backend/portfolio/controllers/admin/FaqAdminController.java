package com.backend.portfolio.controllers.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portfolio.models.requests.FaqRequestInsert;
import com.backend.portfolio.models.responses.FaqResponse;
import com.backend.portfolio.models.updates.FaqRequestUpdate;
import com.backend.portfolio.services.FaqService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Admin endpoints for FAQ - requires authentication
 * Base URL: /api/v1/admin/faqs
 */
@RestController
@RequestMapping("/api/v1/admin/faqs")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class FaqAdminController {

    private final FaqService faqService;

    @GetMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<List<FaqResponse>> getAllFaqs() {
        log.info("Admin request: Get all FAQs");
        List<FaqResponse> faqs = faqService.getAllFaqs();
        return ResponseEntity.ok(faqs);
    }

    @GetMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<FaqResponse> getFaqById(@PathVariable Long id) {
        log.info("Admin request: Get FAQ by id: {}", id);
        FaqResponse faq = faqService.getFaqById(id);
        return ResponseEntity.ok(faq);
    }

    @GetMapping("/profile/{profileId}")
    @RateLimiter(name = "admin")
    public ResponseEntity<List<FaqResponse>> getFaqsByProfileId(@PathVariable Long profileId) {
        log.info("Admin request: Get FAQs by profile id: {}", profileId);
        List<FaqResponse> faqs = faqService.getAllFaqsByProfileId(profileId);
        return ResponseEntity.ok(faqs);
    }

    @PostMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<FaqResponse> createFaq(@Valid @RequestBody FaqRequestInsert request) {
        log.info("Admin request: Create new FAQ");
        FaqResponse faq = faqService.createFaq(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(faq);
    }

    @PutMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<FaqResponse> updateFaq(
            @PathVariable Long id,
            @Valid @RequestBody FaqRequestUpdate request) {
        log.info("Admin request: Update FAQ with id: {}", id);
        FaqResponse faq = faqService.updateFaq(id, request);
        return ResponseEntity.ok(faq);
    }

    @PatchMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<FaqResponse> partialUpdateFaq(
            @PathVariable Long id,
            @RequestBody FaqRequestUpdate request) {
        log.info("Admin request: Partial update FAQ with id: {}", id);
        FaqResponse faq = faqService.updateFaq(id, request);
        return ResponseEntity.ok(faq);
    }

    @DeleteMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<Void> deleteFaq(@PathVariable Long id) {
        log.info("Admin request: Delete FAQ with id: {}", id);
        faqService.deleteFaq(id);
        return ResponseEntity.noContent().build();
    }
}

