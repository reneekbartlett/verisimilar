package com.reneekbartlett.verisimilar.api.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

public class CaseInsensitiveParameterRequestWrapper extends HttpServletRequestWrapper {
    private final Map<String, String[]> FormattedParams = new LinkedHashMap<>();

    public CaseInsensitiveParameterRequestWrapper(HttpServletRequest request) {
        super(request);
        // Force all query keys to UPPERCASE (or lowercase, depending on your naming style)
        request.getParameterMap().forEach((key, value) -> 
            FormattedParams.put(key.toUpperCase(), value)
        );
    }

    @Override
    public String getParameter(String name) {
        String[] values = FormattedParams.get(name.toUpperCase());
        return (values != null && values.length > 0) ? values[0] : null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(FormattedParams);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(FormattedParams.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        return FormattedParams.get(name.toUpperCase());
    }
}
