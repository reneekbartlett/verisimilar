package com.reneekbartlett.verisimilar.api.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.reneekbartlett.verisimilar.api.util.LoggingUtils;

/***
 * Wrapper for the original HttpServletRequest
 */
// Don't use @Component.. configured in SecurityConfig
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);

    private static final String MDC_IP = "clientIp";
    private static final String MDC_TRACE_ID = "traceId";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // Skip logging for any request that does NOT start with /api
        return !path.startsWith("/api");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, 
            HttpServletResponse httpResponse, 
            FilterChain filterChain
    ) throws ServletException, IOException {
        String ipAddress = getClientIp(httpRequest);
        String traceId = httpRequest.getHeader("X-Trace-Id");
        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString().substring(0, 8); // Short unique ID
        }
        MDC.put(MDC_IP, ipAddress);
        MDC.put(MDC_TRACE_ID, traceId);

        // Wrap request and response to allow multiple reads of the body payload
        int cacheLimit = 60;
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpRequest, cacheLimit);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(httpResponse);

        long startTime = System.currentTimeMillis();

        try {
            logRequest(wrappedRequest);

            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logResponse(wrappedResponse, duration);

            // CRITICAL: Copy back the cached response body to the actual client response stream
            // and clear MDC to prevent thread-local memory leaks [not as critical for jdk21+]
            wrappedResponse.copyBodyToResponse();
            MDC.clear();
        }
    }

    // TODO: Look at impl. behind CachedBodyHttpServletRequest where request is WRAPPED, send through, and logged after chain is complete 
    private void logRequest(ContentCachingRequestWrapper request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String params = LoggingUtils.buildParameterString(request);
        LOGGER.info("REQUEST\t {} {}\tparams: {}", method, uri, params);
    }

    private void logResponse(ContentCachingResponseWrapper response, long duration) {
        int status = response.getStatus();

        // Read the body content from the cache wrapper
        byte[] responseBodyBytes = response.getContentAsByteArray();
        String responseBody = new String(responseBodyBytes, StandardCharsets.UTF_8);

        // Truncate response body if it's too large to prevent log flooding
        if (responseBody.length() > 1000) {
            responseBody = responseBody.substring(0, 1000) + "... [Truncated]";
        }

        LOGGER.info("RESPONSE\t{}, Duration: {}ms\tBody: {}", status, duration, responseBody);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // Strip whitespace and return the first client IP in the chain
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

//    private void logCustom(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, long duration) {
//        // 1. Process Request Body (Now filled after Jackson read it)
//        byte[] requestBytes = request.getContentAsByteArray();
//        String requestBody = new String(requestBytes, StandardCharsets.UTF_8);
//        // TODO: String maskedRequestBody = LogMasker.maskJson(requestBody);
//        String maskedRequestBody = requestBody;
//
//        // 2. Process Response Body
//        byte[] responseBytes = response.getContentAsByteArray();
//        String responseBody = new String(responseBytes, StandardCharsets.UTF_8);
//        // TODO: String maskedResponseBody = LogMasker.maskJson(responseBody);
//        String maskedResponseBody = responseBody;
//
//        // Truncate strings if necessary to prevent log flooding
//        if (maskedResponseBody.length() > 1000) {
//            maskedResponseBody = maskedResponseBody.substring(0, 1000) + "... [Truncated]";
//        }
//
//        LOGGER.info("--> Request Body: {}", maskedRequestBody.isEmpty() ? "[EMPTY]" : maskedRequestBody);
//        LOGGER.info("<-- Outgoing Response - Status: {}, Duration: {}ms, Body: {}", response.getStatus(), duration, maskedResponseBody);
//    }
}
