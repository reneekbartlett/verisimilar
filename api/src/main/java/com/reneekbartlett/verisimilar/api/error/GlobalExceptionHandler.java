package com.reneekbartlett.verisimilar.api.error;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.reneekbartlett.verisimilar.api.exception.BadRequestException;
import com.reneekbartlett.verisimilar.api.exception.NotFoundException;
import com.reneekbartlett.verisimilar.api.exception.RateLimitExceededException;
import com.reneekbartlett.verisimilar.api.model.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            BadRequestException ex,
            HttpServletRequest request
    ) {
        ErrorResponse error = new ErrorResponse(400,"Bad Request",ex.getMessage(),request.getRequestURI());
        LOGGER.error("description={}", request.getRequestId(), request.getRequestURI());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiError> handleFileError(IOException ex, WebRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate file.", request);
    }

    // Catch-all for unexpected server errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobal(Exception ex, WebRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", request);
    }

    // Handle Rate Limit Exceeded
    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<Map<String, String>> handleRateLimit(RateLimitExceededException ex, HttpServletResponse response) {
        // Populate standard headers directly into the response
        response.addHeader("X-RateLimit-Limit", String.valueOf(ex.getCapacity()));
        response.addHeader("X-RateLimit-Remaining", "0");
        response.addHeader("X-RateLimit-Reset", String.valueOf(ex.getResetSeconds()));
        response.addHeader("Retry-After", String.valueOf(ex.getRetryAfterSeconds()));

        Map<String, String> errorBody = Map.of(
                "error", "Too Many Requests",
                "message", "You have exceeded your request limit. Please try again in " + ex.getRetryAfterSeconds() + " seconds."
        );

        return new ResponseEntity<>(errorBody, HttpStatus.TOO_MANY_REQUESTS);
    }

    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String message, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        ApiError error = new ApiError(
            Instant.now().toString(),
            status.value(),
            status.getReasonPhrase(),
            message,
            path
        );

        // TODO:  Change ApiError to ErrorResponse & WebRequest to HttpServletRequest? 
        //ErrorResponse errorResponse = new ErrorResponse(400, "Bad Request", message, path);

        LOGGER.error("description={}", request.getDescription(true));

        return new ResponseEntity<>(error, status);
    }

    public record ApiError(
            String timestamp,
            int status,
            String error,
            String message,
            String path
        ) {}
}
