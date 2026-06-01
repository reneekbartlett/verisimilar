package com.reneekbartlett.verisimilar.core.model;

public record AddressLineOne (String streetId, String streetName, String streetSuffix, AddressCategory addressCategory){
    @Override
    public String toString() {
        final String VALUE_DELIM = " ";
        StringBuilder sb = new StringBuilder(0);
        sb.append(this.streetId).append(VALUE_DELIM);
        sb.append(this.streetName).append(VALUE_DELIM);
        sb.append(this.streetSuffix).append(VALUE_DELIM);
        if(addressCategory != null) sb.append(this.addressCategory.getLabel());
        return sb.toString().toUpperCase();
    }
}
