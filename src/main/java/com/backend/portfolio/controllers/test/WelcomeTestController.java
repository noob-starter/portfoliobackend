package com.backend.portfolio.controllers.test;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;

/**
 * Public root endpoint - accessible without authentication
 * Base URL: /api/v1/
 */
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class WelcomeTestController {

    @GetMapping
    @RateLimiter(name = "public")
    public ResponseEntity<Map<String, String>> welcome() {
        log.info("Public request: API root endpoint");
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to Portfolio API");
        response.put("version", "v1");
        response.put("status", "active");
        response.put("documentation", "/swagger-ui.html");
        
        return ResponseEntity.ok(response);
    }
}

