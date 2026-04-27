package com.reneekbartlett.verisimilar.core.model;

public record PostalAddress(String address1, String address2, CityStateZip cityStateZip){

    public PostalAddress(StreetAddress streetAddress, CityStateZip cityStateZip) {
        this(streetAddress.address1(), streetAddress.address2(), cityStateZip);
    }

    public String address1() {
        return address1;
    }

    public String address2() {
        return address2;
    }

    public String city() {
        return cityStateZip.city();
    }

    public String state() {
        return cityStateZip.state();
    }

    public String zip() {
        return cityStateZip.zip();
    }

    public static PostalAddress empty() {
        return new PostalAddress(StreetAddress.empty(), CityStateZip.empty());
    }

    public static PostalAddress placeholder() {
        return new PostalAddress(StreetAddress.placeholder(), CityStateZip.placeholder());
    }

    @Override
    public String toString() {
        final String VALUE_DELIM = " ";
        return new StringBuilder()
            .append(this.address1).append(VALUE_DELIM)
            .append(this.address2).append(this.address2.length() == 0 ? "" : VALUE_DELIM)
            .append(cityStateZip.toString())
            .toString();
    }
}
