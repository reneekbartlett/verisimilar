package com.reneekbartlett.verisimilar.core.model;

import java.util.Set;

public record FullName(String firstName, String middleName, String lastName, GenderIdentity gender) implements CombinationResultField {

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
        //if(gender != null) sb.append(this.gender.name());
        return sb.toString().toUpperCase();
    }

    @Override
    public String toValueString() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<TemplateField> getFields() {
        // TODO Auto-generated method stub
        return null;
    }
}
