package com.reneekbartlett.verisimilar.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.web.util.ContentCachingRequestWrapper;

/***
 * Wrapper for the original HttpServletRequest
 */
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain
    ) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request, 60);

        // Pass the WRAPPED request to the rest of the app
        filterChain.doFilter(wrappedRequest, response);

        // Log the details AFTER the chain is complete
        // The body is only populated in the cache AFTER it has been read by the app
        logRequest(wrappedRequest);
    }

    //private void logRequest(CachedBodyHttpServletRequest request) {
    //    String body = request.toString();
    //    LOGGER.info(body);
    //}

    private void logRequest(ContentCachingRequestWrapper request) {
        //request.getRemoteAddr();
        //CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(request);
        //System.out.println("Processing: " + wrappedRequest.toString());
        String body = new String(request.getContentAsByteArray());
        LOGGER.info(String.format("Method: %s, URI: %s, Body: %s", request.getMethod(), request.getRequestURI(), body));
    }
}
