package com.scanex.dtos;

import java.time.Instant;

public record HealthResponseDTO(
        String status,
        Instant timestamp,
        String service
) {}
