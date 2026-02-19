package com.scanex.dtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponseDTO(
        UUID id,
        String hash,
        String from,
        String to,
        BigDecimal value,
        Instant timestamp
) {}
