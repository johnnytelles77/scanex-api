package com.scanex.dtos.errors;

public record FieldErrorDTO(
        String field,
        String message
) {}
