package org.example.expert.domain.health.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.health.dto.response.HealthCheckResponse;
import org.example.expert.domain.health.service.HealthCheckService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

    @GetMapping("/health-check")
    public ResponseEntity<HealthCheckResponse> healthCheck() {
        if (healthCheckService.healthCheck()) {
            return ResponseEntity.ok(new HealthCheckResponse("UP"));
        }

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new HealthCheckResponse("DOWN"));
    }

}
