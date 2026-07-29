package com.reneekbartlett.verisimilar.api.exception;

public class RateLimitExceededException extends RuntimeException {

    private static final long serialVersionUID = 3314887252166335994L;

    private final long retryAfterSeconds;
    private final long capacity;
    private final long resetSeconds;

    public RateLimitExceededException(long retryAfterSeconds, long capacity, long resetSeconds) {
        super("Too Many Requests");
        this.retryAfterSeconds = retryAfterSeconds;
        this.capacity = capacity;
        this.resetSeconds = resetSeconds;
    }

    public long getRetryAfterSeconds() { 
        return retryAfterSeconds; 
    }

    public long getCapacity() { 
        return capacity; 
    }

    public long getResetSeconds() { 
        return resetSeconds; 
    }
}
