package com.scanex.exceptions;

import com.scanex.dtos.errors.ApiErrorResponseDTO;
import com.scanex.dtos.errors.FieldErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1) Validaciones (@Valid) => 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<FieldErrorDTO> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::mapFieldError)
                .toList();

        ApiErrorResponseDTO body = new ApiErrorResponseDTO(
                "ERROR",
                "Validation failed",
                request.getRequestURI(),
                Instant.now(),
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 2) Error de negocio controlado => 400
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleApiException(
            ApiException ex,
            HttpServletRequest request
    ) {
        ApiErrorResponseDTO body = new ApiErrorResponseDTO(
                "ERROR",
                ex.getMessage(),
                request.getRequestURI(),
                Instant.now(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 3) Error de proveedor externo (Etherscan, etc.) => 502
    @ExceptionHandler(ExternalProviderException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleExternalProviderException(
            ExternalProviderException ex,
            HttpServletRequest request
    ) {
        ApiErrorResponseDTO body = new ApiErrorResponseDTO(
                "ERROR",
                ex.getMessage(),
                request.getRequestURI(),
                Instant.now(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(body);
    }

    // 4) Fallback (cualquier otra excepción) => 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponseDTO> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ) {
        ApiErrorResponseDTO body = new ApiErrorResponseDTO(
                "ERROR",
                "Unexpected error",
                request.getRequestURI(),
                Instant.now(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private FieldErrorDTO mapFieldError(FieldError fe) {
        return new FieldErrorDTO(fe.getField(), fe.getDefaultMessage());
    }
}
