package com.reneekbartlett.verisimilar.api.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import com.reneekbartlett.verisimilar.api.exception.TokenVerificationException;
import com.reneekbartlett.verisimilar.api.model.ApiErrorResponse;
import com.reneekbartlett.verisimilar.api.util.ErrorResponseWriter;

import java.io.IOException;

public class FilterExceptionHandler extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TokenVerificationException ex) {
            ApiErrorResponse errorPayload = ErrorResponseWriter.buildResponse(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized",
                    ex.getMessage(),
                    request.getServletPath()
            );
            
            ErrorResponseWriter.writeJsonToResponse(response, errorPayload);
        }
    }
}
