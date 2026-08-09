package com.reneekbartlett.verisimilar.api.security.filter;

//import com.reneekbartlett.verisimilar.api.model.ApiErrorResponse;
import com.reneekbartlett.verisimilar.api.security.ApiKeyAuthToken;
import com.reneekbartlett.verisimilar.api.security.ApiKeyProperties;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//import tools.jackson.databind.ObjectMapper;

public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiKeyAuthFilter.class);

    // TODO
    // 256-bit AES shared secret key (Must match client key exactly)
    // Production note: Inject this using @Value from an encrypted vault or environment variable
    //private static final byte[] SHARED_SECRET_256_BIT = "".getBytes();
    //private static final long MAX_ALLOWED_AGE_SECONDS = 60; // 1-minute validity window

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
        if (apiKey != null && !apiKey.isBlank() && SecurityContextHolder.getContext().getAuthentication() == null) {
            Authentication authResult;
            try {
                ApiKeyAuthToken authRequest = new ApiKeyAuthToken(apiKey);
                authResult = authenticationManager.authenticate(authRequest);

                // TODO: Remove?
                if(authResult.isAuthenticated()) {
                    //List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_API_CLIENT"));
                    // TODO: ??
                    //UserDetails userDetails = new User("VsimApiClient", "", authorities);
                    LOGGER.debug("authResult.isAuthenticated()={}", authResult.isAuthenticated());
                } else {
                    LOGGER.debug("authResult.isAuthenticated()={}", authResult.isAuthenticated());
                }

                // Store authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authResult);
            } catch (Exception ex) {
                LOGGER.debug("invalid API Key");

                // 6. Authentication failed → return JSON 400
                writeJsonError(request, response, 401, "Invalid API Key");
                return;
            }
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
        //ApiErrorResponse error = new ApiErrorResponse(status, HttpStatus.valueOf(status).getReasonPhrase(), message, request.getRequestURI());
        response.setStatus(status);
        response.setContentType("application/json");
        //TODO: Write ApiResponse with objectMapper?
        //response.getWriter().write(new ObjectMapper().writeValueAsString(error));
        response.getWriter().write("Invalid or missing X-API-Key header.");
    }
}
