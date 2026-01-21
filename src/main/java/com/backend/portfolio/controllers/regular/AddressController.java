package com.backend.portfolio.controllers.regular;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portfolio.models.responses.AddressResponse;
import com.backend.portfolio.services.AddressService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Public endpoints for Address - accessible without authentication
 * Base URL: /api/v1/addresses
 */
@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
@Slf4j
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/profile/{profileId}")
    @RateLimiter(name = "public")
    public ResponseEntity<List<AddressResponse>> getAddressesByProfileId(@PathVariable Long profileId) {
        log.info("Public request: Get addresses by profile id: {}", profileId);
        List<AddressResponse> addresses = addressService.getAllAddressesByProfileId(profileId);
        return ResponseEntity.ok(addresses);
    }
}

