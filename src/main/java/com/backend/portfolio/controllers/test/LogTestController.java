package com.backend.portfolio.controllers.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Test controller for generating various log levels to test ELK stack integration.
 * This controller should be used only in development/testing environments.
 */
@RestController
@RequestMapping("/admin/test/logs")
@Slf4j
public class LogTestController {

    @GetMapping("/all")
    public ResponseEntity<Map<String, String>> generateAllLogs() {
        log.debug("DEBUG: Detailed debugging information");
        log.info("INFO: General application flow information");
        log.warn("WARN: Warning - potential issue detected");
        log.error("ERROR: Error occurred in the application");
        
        try {
            throw new RuntimeException("Sample exception for testing");
        } catch (Exception e) {
            log.error("ERROR with exception: Exception caught during all logs test", e);
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "All log levels generated successfully");
        response.put("levels", "DEBUG, INFO, WARN, ERROR, ERROR with Exception");
        response.put("timestamp", java.time.Instant.now().toString());
        
        return ResponseEntity.ok(response);
    }
}

