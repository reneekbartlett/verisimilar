package com.reneekbartlett.verisimilar.core.model;

public record StreetAddress(String address1, String address2, String type){

    public static StreetAddress empty() {
        return new StreetAddress(null, null, null);
    }

    public static StreetAddress placeholder() {
        return new StreetAddress("301 MASSACHUSETTS AVE", "UNIT 2", null);
    }

    @Override
    public String toString() {
        final String VALUE_DELIM = " ";
        StringBuilder sb = new StringBuilder(0);
        if(address1 != null) sb.append(this.address1);
        if(address2 != null) sb.append(VALUE_DELIM).append(this.address2);
        return sb.toString();
    }
}
