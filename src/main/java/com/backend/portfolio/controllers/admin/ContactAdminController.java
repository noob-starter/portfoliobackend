package com.backend.portfolio.controllers.admin;

import com.backend.portfolio.models.requests.ContactRequestInsert;
import com.backend.portfolio.models.responses.ContactResponse;
import com.backend.portfolio.models.updates.ContactRequestUpdate;
import com.backend.portfolio.services.ContactService;
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
 * Admin endpoints for Contact - requires authentication
 * Base URL: /api/v1/admin/contacts
 */
@RestController
@RequestMapping("/api/v1/admin/contacts")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class ContactAdminController {

    private final ContactService contactService;

    @GetMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<List<ContactResponse>> getAllContacts() {
        log.info("Admin request: Get all contacts");
        List<ContactResponse> contacts = contactService.getAllContacts();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<ContactResponse> getContactById(@PathVariable Long id) {
        log.info("Admin request: Get contact by id: {}", id);
        ContactResponse contact = contactService.getContactById(id);
        return ResponseEntity.ok(contact);
    }

    @GetMapping("/profile/{profileId}")
    @RateLimiter(name = "admin")
    public ResponseEntity<List<ContactResponse>> getContactsByProfileId(@PathVariable Long profileId) {
        log.info("Admin request: Get contacts by profile id: {}", profileId);
        List<ContactResponse> contacts = contactService.getAllContactsByProfileId(profileId);
        return ResponseEntity.ok(contacts);
    }

    @PostMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<ContactResponse> createContact(@Valid @RequestBody ContactRequestInsert request) {
        log.info("Admin request: Create new contact");
        ContactResponse contact = contactService.createContact(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(contact);
    }

    @PutMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<ContactResponse> updateContact(
            @PathVariable Long id,
            @Valid @RequestBody ContactRequestUpdate request) {
        log.info("Admin request: Update contact with id: {}", id);
        ContactResponse contact = contactService.updateContact(id, request);
        return ResponseEntity.ok(contact);
    }

    @PatchMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<ContactResponse> partialUpdateContact(
            @PathVariable Long id,
            @RequestBody ContactRequestUpdate request) {
        log.info("Admin request: Partial update contact with id: {}", id);
        ContactResponse contact = contactService.updateContact(id, request);
        return ResponseEntity.ok(contact);
    }

    @DeleteMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        log.info("Admin request: Delete contact with id: {}", id);
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}

