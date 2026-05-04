//package com.reneekbartlett.verisimilar.api.filter;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletRequestWrapper;
//import java.util.stream.Collectors;
//
///**
// * A custom wrapper to provide a meaningful string representation of the request.
// */
//public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
//
//    public CachedBodyHttpServletRequest(HttpServletRequest request) {
//        super(request);
//    }
//
//    @Override
//    public String toString() {
//        // TODO:  Add remoteUser/sessionId/IP/cookies?
//        //String remoteUser = super.getRemoteUser();
//        //String sessionId = getSession().getId();
//
//        // Collect parameters into a readable string
//        String params = getParameterMap().entrySet().stream()
//                .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
//                .collect(Collectors.joining(", ", "{", "}"));
//
//        return String.format("Request[Method=%s, URI=%s, Params=%s]", getMethod(), getRequestURI(), params);
//    }
//}
