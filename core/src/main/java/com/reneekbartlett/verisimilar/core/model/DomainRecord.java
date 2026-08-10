package com.reneekbartlett.verisimilar.core.model;

public record DomainRecord (String domain, DomainType domainType) implements ResultRecord {

    @Override
    public String toValueString() {
        return this.toString();
    }

    @Override
    public String toString() {
        final String VALUE_DELIM = " ";
        StringBuilder sb = new StringBuilder(0);
        sb.append(this.domain).append(VALUE_DELIM);
        if(domainType != null) sb.append(this.domainType.getLabel());
        return sb.toString().toUpperCase();
    }
}
