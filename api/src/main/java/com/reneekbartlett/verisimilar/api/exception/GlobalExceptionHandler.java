package com.reneekbartlett.verisimilar.api.exception;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.reneekbartlett.verisimilar.api.model.ApiErrorResponse;
import com.reneekbartlett.verisimilar.api.model.ApiSubError;
import com.reneekbartlett.verisimilar.api.model.FieldValidationError;
import com.reneekbartlett.verisimilar.api.util.ErrorResponseWriter;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(TokenVerificationException.class)
    public ResponseEntity<ApiErrorResponse> handleTokenException(TokenVerificationException e, HttpServletRequest request) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(
            AccessDeniedException e, HttpServletRequest request) {
        return createErrorResponse(HttpStatus.FORBIDDEN, "You do not have permission to execute this operation.", request);
    }

    // HTTP 400 Bad Request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(
            BadRequestException e,
            HttpServletRequest request
    ) {
        LOGGER.error("{}    BAD REQUEST    requestURI={}", request.getRequestId(), request.getRequestURI());
        return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<ApiSubError> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldValidationError(
                        error.getField(),
                        error.getRejectedValue() == null ? "null" : error.getRejectedValue().toString(),
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed for one or more fields.", request, errors);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException e, WebRequest request) {
        String requestId = "????"; // TODO
        String path = request.getDescription(false).replace("uri=", "");
        LOGGER.error("{}    NOT FOUND    path={}", requestId, path);
        return createApiResponse(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiErrorResponse> handleFileError(IOException e, WebRequest request) {
        String requestId = "????"; // TODO
        String path = request.getDescription(false).replace("uri=", "");
        LOGGER.error("{}    INTERNAL_SERVER_ERROR    path={}", requestId, path);
        return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate file.", request);
    }

    // Handle Rate Limit Exceeded
    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ApiErrorResponse> handleRateLimit(RateLimitExceededException e, HttpServletResponse response) {
        // Populate standard headers directly into the response
        response.addHeader("X-RateLimit-Limit", String.valueOf(e.getCapacity()));
        response.addHeader("X-RateLimit-Remaining", "0");
        response.addHeader("X-RateLimit-Reset", String.valueOf(e.getResetSeconds()));
        response.addHeader("Retry-After", String.valueOf(e.getRetryAfterSeconds()));

        String message = "You have exceeded your request limit. Please try again in " + e.getRetryAfterSeconds() + " seconds.";

        String requestId = "????";
        LOGGER.error("{}    TOO MANY REQUESTS    message={}", requestId, e.getMessage(), e);

        //return createApiResponse(HttpStatus.TOO_MANY_REQUESTS, message, null);
        return createErrorResponse(HttpStatus.TOO_MANY_REQUESTS, message, null);
    }

    // Catch-all for unexpected server errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllUnexpectedExceptions(Exception e, HttpServletRequest request) {
        LOGGER.error("{}    UNEXPECTED EXCEPTION    message={}", request.getRequestId(), e.getMessage(), e);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", request);
    }

    private ResponseEntity<ApiErrorResponse> createApiResponse(HttpStatus status, String message, WebRequest request) {
        String path = (request == null) ? "" : request.getDescription(false).replace("uri=", "");
        ApiErrorResponse error = new ApiErrorResponse(
            status.value(),
            status.getReasonPhrase(),
            message,
            path
        );
        return new ResponseEntity<>(error, status);
    }

    private ResponseEntity<ApiErrorResponse> createErrorResponse(HttpStatus status, 
            String message, 
            HttpServletRequest request, 
            List<ApiSubError> errors
    ) {
        String requestUri = (request == null) ? "" : request.getRequestURI();
        ApiErrorResponse error = ErrorResponseWriter.buildValidationErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                requestUri,
                errors
        );
        return new ResponseEntity<>(error, status);
    }

    private ResponseEntity<ApiErrorResponse> createErrorResponse(HttpStatus status, String message, HttpServletRequest request) {
        String requestUri = (request == null) ? "" : request.getRequestURI();
        ApiErrorResponse error = ErrorResponseWriter.buildResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                requestUri
        );
        return new ResponseEntity<>(error, status);
    }
}
