package com.reneekbartlett.verisimilar.core.model;

/***
 * Combined field containing address1 (String), StreetId, StreetName, StreetSuffix, AddressCategory
 */
public record AddressLineOne (String address1, String streetId, String streetName, StreetSuffix streetSuffix, AddressCategory addressCategory){

    public static AddressLineOne empty() {
        return new AddressLineOne(null, null, null, null, AddressCategory.EMPTY);
    }

    public static AddressLineOne placeholder() {
        //return new AddressLineOne("11", "MULBERRY", "LANE", AddressCategory.SINGLE_FAMILY);
        return new AddressLineOne("301 MASSACHUSETTS AVE", "301", "MASSACHUSETTS", StreetSuffix.AVENUE, AddressCategory.SINGLE_FAMILY);
    }

    @Override
    public String toString() {
        final String VALUE_DELIM = " ";
        StringBuilder sb = new StringBuilder(0);
        if(streetId != null) sb.append(this.streetId).append(VALUE_DELIM);
        if(streetName != null) sb.append(this.streetName).append(VALUE_DELIM);
        if(streetSuffix != null) sb.append(this.streetSuffix.getLabel());
        //if(addressCategory != null) sb.append(this.addressCategory.getLabel());
        return sb.toString().toUpperCase();
    }
}
