package com.reneekbartlett.verisimilar.api.exception;

public class TokenVerificationException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 2127962902365166060L;

    public TokenVerificationException(String message) {
        super(message);
    }

    public TokenVerificationException(String message, Throwable cause) {
        super(message, cause);
    }

}
