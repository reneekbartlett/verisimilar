package com.reneekbartlett.verisimilar.api.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorResponse(
    String timestamp,
    int status,
    String error,
    String message,
    String path,
    List<ApiSubError> fieldErrors
) {

    public ApiErrorResponse(int status, String error, String message, String path) {
        this(LocalDateTime.now().toString(), status, error, message, path, null);
    }

    public ApiErrorResponse(Instant ts, int status, String error, String message, String path) {
        this(ts.toString(), status, error, message, path, null);
    }

    public ApiErrorResponse(int status, String error, String message, String path, List<ApiSubError> fieldErrors) {
        this(LocalDateTime.now().toString(), status, error, message, path, fieldErrors);
    }

    public static ApiErrorResponse of(int status, String error, String message, String path) {
        return new ApiErrorResponse(Instant.now().toString(), status, error,  message, path, null);
    }
}
