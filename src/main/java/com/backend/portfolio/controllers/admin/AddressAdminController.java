package com.backend.portfolio.controllers.admin;

import com.backend.portfolio.models.requests.AddressRequestInsert;
import com.backend.portfolio.models.responses.AddressResponse;
import com.backend.portfolio.models.updates.AddressRequestUpdate;
import com.backend.portfolio.services.AddressService;
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
 * Admin endpoints for Address - requires authentication
 * Base URL: /api/v1/admin/addresses
 */
@RestController
@RequestMapping("/api/v1/admin/addresses")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AddressAdminController {

    private final AddressService addressService;

    @GetMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<List<AddressResponse>> getAllAddresses() {
        log.info("Admin request: Get all addresses");
        List<AddressResponse> addresses = addressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<AddressResponse> getAddressById(@PathVariable Long id) {
        log.info("Admin request: Get address by id: {}", id);
        AddressResponse address = addressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }

    @GetMapping("/profile/{profileId}")
    @RateLimiter(name = "admin")
    public ResponseEntity<List<AddressResponse>> getAddressesByProfileId(@PathVariable Long profileId) {
        log.info("Admin request: Get addresses by profile id: {}", profileId);
        List<AddressResponse> addresses = addressService.getAllAddressesByProfileId(profileId);
        return ResponseEntity.ok(addresses);
    }

    @PostMapping
    @RateLimiter(name = "admin")
    public ResponseEntity<AddressResponse> createAddress(@Valid @RequestBody AddressRequestInsert request) {
        log.info("Admin request: Create new address");
        AddressResponse address = addressService.createAddress(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(address);
    }

    @PutMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequestUpdate request) {
        log.info("Admin request: Update address with id: {}", id);
        AddressResponse address = addressService.updateAddress(id, request);
        return ResponseEntity.ok(address);
    }

    @PatchMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<AddressResponse> partialUpdateAddress(
            @PathVariable Long id,
            @RequestBody AddressRequestUpdate request) {
        log.info("Admin request: Partial update address with id: {}", id);
        AddressResponse address = addressService.updateAddress(id, request);
        return ResponseEntity.ok(address);
    }

    @DeleteMapping("/{id}")
    @RateLimiter(name = "admin")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        log.info("Admin request: Delete address with id: {}", id);
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}

