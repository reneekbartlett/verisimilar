package com.reneekbartlett.verisimilar.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class NotFoundRedirectFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotFoundRedirectFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain) throws ServletException, IOException {

        // Example: Check if the request looks like a frontend route that doesn't exist
        String uri = request.getRequestURI();

        LOGGER.debug("uri={}", uri);

        // Adjust this logic to fit your specific URL structure
        if (!uri.startsWith("/api") && !uri.contains(".")) {
            // Forward natively inside the container so the user stays on the URL
            request.getRequestDispatcher("/index.html").forward(request, response);
            return; 
        }

        filterChain.doFilter(request, response);
    }
}
