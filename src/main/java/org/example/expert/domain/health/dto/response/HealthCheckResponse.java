package org.example.expert.domain.health.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HealthCheckResponse {

    private final String status;
}
