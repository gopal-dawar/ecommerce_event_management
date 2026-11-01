package com.ecommerce_event_management.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Structured error response payload returned by GlobalExceptionHandler.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    @Builder.Default
    private boolean success = false;

    private String message;

    private String errorCode;

    private Integer statusCode;

    private String path;

    private Map<String, String> validationErrors;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}

