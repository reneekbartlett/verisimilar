package com.reneekbartlett.verisimilar.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reneekbartlett.verisimilar.api.model.ApiErrorResponse;
import com.reneekbartlett.verisimilar.api.model.ApiSubError;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import java.io.IOException;
import java.util.List;

public class ErrorResponseWriter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ErrorResponseWriter() {}

    public static ApiErrorResponse buildResponse(int status, String error, String message, String path) {
        return new ApiErrorResponse(status, error, message, path);
    }

    public static ApiErrorResponse buildValidationErrorResponse(
            int status, String error, String message, String path, List<ApiSubError> fieldErrors) {
        return new ApiErrorResponse(status, error, message, path, fieldErrors);
    }

    public static void writeJsonToResponse(HttpServletResponse response, ApiErrorResponse errorPayload) throws IOException {
        response.setStatus(errorPayload.status());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), errorPayload);
    }
}
