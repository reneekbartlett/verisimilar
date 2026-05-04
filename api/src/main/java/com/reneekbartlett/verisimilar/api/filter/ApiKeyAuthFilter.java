package com.reneekbartlett.verisimilar.api.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.reneekbartlett.verisimilar.api.model.ErrorResponse;
import com.reneekbartlett.verisimilar.api.security.api.ApiKeyAuthenticationToken;
import com.reneekbartlett.verisimilar.api.security.api.ApiKeyProperties;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiKeyAuthFilter.class);

    private final AuthenticationManager authenticationManager;
    private final ApiKeyProperties properties;

    public ApiKeyAuthFilter(AuthenticationManager authenticationManager, ApiKeyProperties properties) {
        this.authenticationManager = authenticationManager;
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        // Try header first
        String apiKey = request.getHeader("X-API-Key");

        // Allow URL-based API keys only if enabled (local/dev)
        if (apiKey == null && properties.isAllowUrlApiKeys()) {
            apiKey = request.getParameter("api_key");
        }

        // If no key provided → skip authentication
        if (apiKey == null || apiKey.isBlank()) {
            chain.doFilter(request, response);
            return;
        }

        try {
            // Authenticate using your provider
            var authRequest = new ApiKeyAuthenticationToken(apiKey);
            var authResult = authenticationManager.authenticate(authRequest);

            // 5. Store authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authResult);

        } catch (Exception ex) {
            LOGGER.debug("invalid API Key");

            // 6. Authentication failed → return JSON 400
            writeJsonError(request, response, 400, "Invalid API Key");
            return;
        }

        // 7. Continue filter chain
        chain.doFilter(request, response);
    }

    private void writeJsonError(
            HttpServletRequest request, 
            HttpServletResponse response, 
            int status, 
            String message
   ) throws IOException {
        ErrorResponse error = new ErrorResponse(status, HttpStatus.valueOf(status).getReasonPhrase(), message, request.getRequestURI());
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(error));
    }
}
