package com.reneekbartlett.verisimilar.core.model;

public record PhoneNumber(String areaCode, String exchangeCode, String lineNumber){

    public static PhoneNumber empty() {
        return new PhoneNumber(null, null, null);
    }

    public static PhoneNumber placeholder() {
        return new PhoneNumber("617", "536", "9000");
    }

    @Override
    public String toString() {
        final String VALUE_DELIM = "-";
        return new StringBuilder()
            .append(this.areaCode).append(VALUE_DELIM)
            .append(this.exchangeCode).append(VALUE_DELIM)
            .append(this.lineNumber)
            .toString();
    }
}
