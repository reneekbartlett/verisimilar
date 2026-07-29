package com.reneekbartlett.verisimilar.api.exception;

public class RateLimitException extends RuntimeException {

    private static final long serialVersionUID = 8731500463402992905L;

    public RateLimitException(String message) {
        super(message);
    }
}
