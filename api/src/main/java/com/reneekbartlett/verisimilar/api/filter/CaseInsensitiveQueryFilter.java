package com.reneekbartlett.verisimilar.api.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import com.reneekbartlett.verisimilar.api.config.CaseInsensitiveParameterRequestWrapper;

import java.io.IOException;

@Component
public class CaseInsensitiveQueryFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            // Wrap the request so parameter names match your @RequestParam(name="STATE") uppercase declaration
            chain.doFilter(new CaseInsensitiveParameterRequestWrapper((HttpServletRequest) request), response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
