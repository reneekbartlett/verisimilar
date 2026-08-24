package com.reneekbartlett.verisimilar.core.model;

/***
 * Hyphenated Address Ranges
 * 
 * Grid Style Addresses: https://pe.usps.com/text/pub28/28apd_003.htm
 * contain significant punctuation, such as periods (for example, 39.2 RD, 39.4 RD). 
 * grid style addresses in Salt Lake City that include double directionals 
 * (for example, in 842 E 1700 S: E is a predirectional, S is a postdirectional, and 1700 is located in the street name field).
 * 
 * Numeric street names, 
 * For example, 7TH ST or SEVENTH ST, should be output on the mail piece exactly as they appear in the ZIP+4 file.
 * Spell out numeric street names only when there are duplicate street names within a postal delivery area and the only distinguishing factor is 
 * that the one you matched is spelled out.
 * 
 * Corner Addresses https://pe.usps.com/text/pub28/28c2_017.htm
 * 514 HIGH ST
 * 5TH AND HIGH
 * 
 * Military: CPR (Consolidated Postal Room), OPC (Official Postal Center), PSC (Postal Service Center), UPR (Unit Postal Room), and UNIT.
 * 
 * Department of State Addresses    https://pe.usps.com/text/pub28/28c2_020.htm
 * Rural Route Addresses [Leading 0, hyphens, RFD/RD->RR]
 * Highway Contract Route Addresses HC ## BOX ##
 */
public record StreetAddress(AddressLineOne addressLineOne, AddressLineTwo addressLineTwo, AddressCategory addressCategory){

    public StreetAddress(
            String address1,
            String address2,
            AddressLineOne addressLineOne,
            AddressLineTwo addressLineTwo
    ){
        this(addressLineOne, addressLineTwo, addressLineOne.addressCategory());
    }

    public static StreetAddress empty() {
        return new StreetAddress(AddressLineOne.empty(), AddressLineTwo.empty(), AddressCategory.EMPTY);
    }

    public static StreetAddress placeholder() {
        AddressCategory addressCategory = AddressCategory.SINGLE_FAMILY;
        //UnitType unitType = UnitType.APARTMENT;
        //AddressLineOne addressLineOne = new AddressLineOne(
        //        "301 MASSACHUSETTS AVE", "301", //"STREET_ID",
        //        "MASSACHUSETTS", //"STREET_NAME",
        //        StreetSuffix.AVENUE, //"STREET_SUFFIX",
        //        addressCategory);
        //AddressLineTwo addressLineTwo = new AddressLineTwo("UNIT 2",
        //        "2", //"UNIT_NUMBER",
        //        null, //"UNIT_XTRA",
        //        unitType,
        //        addressCategory);

        return new StreetAddress(
                AddressLineOne.placeholder(), 
                AddressLineTwo.placeholder(), 
                addressCategory
        );
    }

    @Override
    public String toString() {
        final String VALUE_DELIM = " ";
        StringBuilder sb = new StringBuilder(0);
        if(addressLineOne != null) sb.append(this.addressLineOne.toString());
        if(addressLineTwo != null) sb.append(VALUE_DELIM).append(this.addressLineTwo);
        return sb.toString();
    }
}
