package com.scanex.dtos.errors;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponseDTO(
        String status,
        String message,
        String path,
        Instant timestamp,
        List<FieldErrorDTO> errors
) {}