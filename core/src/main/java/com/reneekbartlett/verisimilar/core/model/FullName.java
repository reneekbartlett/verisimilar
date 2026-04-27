package com.reneekbartlett.verisimilar.core.model;

public record FullName(String firstName, String middleName, String lastName){

    public static FullName empty() {
        return new FullName(null, null, null);
    }

    public static FullName placeholder() {
        return new FullName("THOMAS", "MICHAEL", "MENINO");
    }

    @Override
    public String toString() {
        final String VALUE_DELIM = " ";
        return new StringBuilder()
            .append(this.firstName).append(VALUE_DELIM)
            .append(this.middleName).append(VALUE_DELIM)
            .append(this.lastName).append(VALUE_DELIM)
            .toString().toUpperCase();
    }
}
