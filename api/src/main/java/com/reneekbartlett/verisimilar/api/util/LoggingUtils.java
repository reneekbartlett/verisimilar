package com.reneekbartlett.verisimilar.api.util;

import java.util.Collections;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoggingUtils {

    private LoggingUtils() {
        //
    }

    /***
     * NOTE: Response can have more than one header with the same name
     * @param response
     * @return String with header values
     */
    public static String buildHeadersString(HttpServletResponse response) {
        return response.getHeaderNames().stream()
                .map(name -> name + ": " + String.join(", ", response.getHeaders(name)))
                .collect(Collectors.joining("\n"));
    }

    public static String buildHeadersString(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames()).stream()
                .map(name -> name + ": " + String.join(", ", request.getHeader(name)))
                .collect(Collectors.joining("\n"));
    }

    public static String buildParameterString(HttpServletRequest request) {
        return Collections.list(request.getParameterNames()).stream()
                .map(name -> name + ": " + String.join(", ", request.getParameterValues(name)))
                .collect(Collectors.joining("\n"));
    }

}
