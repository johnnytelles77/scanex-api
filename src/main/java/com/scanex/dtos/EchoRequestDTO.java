package com.scanex.dtos;
import jakarta.validation.constraints.NotBlank;

public record EchoRequestDTO(
        @NotBlank String message
) {}
