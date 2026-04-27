package com.reneekbartlett.verisimilar.core.model;

//public record Zip4Range<T1,T2,T3,T4,T5>(String zip5, String zip4Start, String zip4End, String type, String notes){
public record Zip4Range(String zip5, String zip4Start, String zip4End, String type, String notes){
    @Override
    public String toString() {
        final String VALUE_DELIM = "";
        return new StringBuilder()
            .append(this.zip5).append(VALUE_DELIM)
            .append(this.zip4Start).append(VALUE_DELIM)
            .append(this.zip4End).append(VALUE_DELIM)
            .append(this.notes).append(VALUE_DELIM)
            .toString().toUpperCase();
    }
}
