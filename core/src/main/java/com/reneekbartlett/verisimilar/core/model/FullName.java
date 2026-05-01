package com.reneekbartlett.verisimilar.core.model;

public record FullName(String firstName, String middleName, String lastName, GenderIdentity gender){

    public FullName(String firstName, String middleName, String lastName){
        this(firstName, middleName, lastName, null);
    }

    public static FullName empty() {
        return new FullName(null, null, null);
    }

    public static FullName placeholder() {
        return new FullName("THOMAS", "MICHAEL", "MENINO");
    }

    @Override
    public String toString() {
        final String VALUE_DELIM = " ";
        StringBuilder sb = new StringBuilder(0);
        sb.append(this.firstName).append(VALUE_DELIM);
        sb.append(this.middleName).append(VALUE_DELIM);
        sb.append(this.lastName).append(VALUE_DELIM);
        if(gender != null) sb.append(this.gender.name());
        return sb.toString().toUpperCase();
    }
}
