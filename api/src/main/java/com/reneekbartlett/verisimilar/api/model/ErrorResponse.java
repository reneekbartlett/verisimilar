package com.reneekbartlett.verisimilar.api.model;

import java.time.Instant;

public record ErrorResponse(
        int status,
        String error,
        String message,
        String path,
        Instant timestamp
) {
    public ErrorResponse(int status, String error, String message, String path) {
        this(status, error, message, path, Instant.now());
    }

    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(status, error,  message, path, Instant.now());
    };
}
