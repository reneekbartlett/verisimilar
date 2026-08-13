package com.reneekbartlett.verisimilar.api.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.stereotype.Component;
import java.io.IOException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/***
 * Filter intercepts the raw request before it reaches Spring MVC's argument resolution phase.
 */
@Component
public class QueryParameterNormalizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest) {
            String queryString = httpRequest.getQueryString();

            // Check if the query string contains unencoded brackets
            if (queryString != null && (queryString.contains("[") || queryString.contains("]"))) {
                // Wrap the request to present cleanly encoded parameters to the Resolver
                chain.doFilter(new EncodedParameterRequestWrapper(httpRequest), response);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private static class EncodedParameterRequestWrapper extends HttpServletRequestWrapper {
        private Map<String, String[]> parameterMap;

        public EncodedParameterRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            if (parameterMap == null) {
                Map<String, String[]> originalMap = super.getParameterMap();
                Map<String, String[]> formattedMap = new LinkedHashMap<>();

                for (Map.Entry<String, String[]> entry : originalMap.entrySet()) {
                    // Encode the parameter keys (e.g., "filter[name]" -> "filter%5Bname%5D")
                    String safeKey = encodeBrackets(entry.getKey());

                    // Encode the parameter values if they contain raw brackets
                    String[] originalValues = entry.getValue();
                    String[] safeValues = new String[originalValues.length];
                    for (int i = 0; i < originalValues.length; i++) {
                        safeValues[i] = encodeBrackets(originalValues[i]);
                    }
                    formattedMap.put(safeKey, safeValues);
                }
                parameterMap = Collections.unmodifiableMap(formattedMap);
            }
            return parameterMap;
        }

        @Override
        public String getParameter(String name) {
            String[] values = getParameterMap().get(name);
            return (values != null && values.length > 0) ? values[0] : super.getParameter(name);
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return Collections.enumeration(getParameterMap().keySet());
        }

        @Override
        public String[] getParameterValues(String name) {
            return getParameterMap().get(name);
        }

        private String encodeBrackets(String input) {
            if (input == null) return null;
            // Explicitly targets target relaxed characters for standard RFC compliance
            return input.replace("[", "%5B").replace("]", "%5D");
        }
    }
}
