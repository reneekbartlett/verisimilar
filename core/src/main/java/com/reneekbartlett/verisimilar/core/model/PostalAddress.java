package com.reneekbartlett.verisimilar.core.model;

import java.util.Set;

/***
 * Combined field containing AddressLineOne, AddressLineTwo, CityStateZip, AddressCategory
 */
public record PostalAddress(
        AddressLineOne addressLineOne,
        AddressLineTwo addressLineTwo,
        CityStateZip cityStateZip,
        AddressCategory addressCategory
) implements CombinationResultField {

    public PostalAddress(StreetAddress streetAddress, CityStateZip cityStateZip) {
        this(streetAddress.addressLineOne(), streetAddress.addressLineTwo(), cityStateZip, streetAddress.addressCategory());
    }

    public StreetAddress streetAddress() {
        return new StreetAddress(address1(), address2(), addressCategory());
    }

    public AddressLineOne address1() {
        return addressLineOne;
    }

    public AddressLineTwo address2() {
        return addressLineTwo;
    }

    public String city() {
        return cityStateZip.city();
    }

    public USState state() {
        return USState.fromText(cityStateZip.state());
    }

    public String zip() {
        return cityStateZip.zip();
    }

    public AddressCategory addressCategory() {
        return addressCategory;
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
        StringBuilder sb = new StringBuilder(0);
        if(addressLineOne != null) sb.append(this.addressLineOne.toString()).append(VALUE_DELIM);
        if(addressLineTwo != null) sb.append(this.addressLineTwo.toString()).append(VALUE_DELIM);
        if(cityStateZip != null) sb.append(cityStateZip.toString()).append(VALUE_DELIM);
        return sb.toString();
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
