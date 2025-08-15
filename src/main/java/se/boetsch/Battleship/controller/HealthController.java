package se.boetsch.Battleship.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/// Endpoint to check if the server is responding
@RestController
@RequestMapping("/health")
class HealthController {
    @GetMapping
    public ResponseEntity<?> ok() {
        return ResponseEntity.ok().body(Map.of("status", "UP"));
    }
}