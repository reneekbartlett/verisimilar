package com.reneekbartlett.verisimilar.api.exception;

public class ThirdPartyApiException extends RuntimeException {
    private final int statusCode;
    private final String errorBody;

    public ThirdPartyApiException(String message, int statusCode, String errorBody) {
        super(message);
        this.statusCode = statusCode;
        this.errorBody = errorBody;
    }

    public int getStatusCode() { return statusCode; }
    public String getErrorBody() { return errorBody; }
}
