package com.backend.portfolio.controllers.regular;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portfolio.models.responses.ContactResponse;
import com.backend.portfolio.services.ContactService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Public endpoints for Contact - accessible without authentication
 * Base URL: /api/v1/contacts
 */
@RestController
@RequestMapping("/api/v1/contacts")
@RequiredArgsConstructor
@Slf4j
public class ContactController {

    private final ContactService contactService;

    @GetMapping("/profile/{profileId}")
    @RateLimiter(name = "public")
    public ResponseEntity<List<ContactResponse>> getContactsByProfileId(@PathVariable Long profileId) {
        log.info("Public request: Get contacts by profile id: {}", profileId);
        List<ContactResponse> contacts = contactService.getAllContactsByProfileId(profileId);
        return ResponseEntity.ok(contacts);
    }
}

