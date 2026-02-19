package com.scanex.dtos;

import com.scanex.domain.enums.Chain;

import java.time.Instant;
import java.util.UUID;

public record AddressResponseDTO(
        UUID id,
        Chain chain,
        String address,
        Instant firstSeen,
        Instant lastSeen
) {}
