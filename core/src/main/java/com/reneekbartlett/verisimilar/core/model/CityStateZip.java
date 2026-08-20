package com.reneekbartlett.verisimilar.core.model;

public record CityStateZip(String city, String state, String zip){

    public CityStateZip(String city, USState state, String zip, String id){
        this(city, state.getLabel(), zip);
    }

    public static CityStateZip empty() {
        return new CityStateZip(null, null, null);
    }

    public static CityStateZip placeholder() {
        return new CityStateZip("BOSTON", "MA", "02116");
    }

    @Override
    public String toString() {
        final String VALUE_DELIM = " ";
        StringBuilder sb = new StringBuilder(0);
        if(city != null) sb.append(this.city).append(VALUE_DELIM);
        if(state != null) sb.append(this.state).append(VALUE_DELIM);
        if(zip != null) sb.append(this.zip).append(VALUE_DELIM);
        return sb.toString();
    }
}
