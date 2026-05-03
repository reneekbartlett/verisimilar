package com.reneekbartlett.verisimilar.api.security.api;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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

        String apiKey = request.getHeader("X-API-Key");

        // Allow URL-based API keys only in local/dev
        if (apiKey == null && properties.isAllowUrlApiKeys()) {
            apiKey = request.getParameter("api_key");
        }

        if (apiKey == null) {
            chain.doFilter(request, response);
            return;
        }

        LOGGER.debug("KEY OK");

        var authRequest = new ApiKeyAuthenticationToken(apiKey);
        var authResult = authenticationManager.authenticate(authRequest);

        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}
