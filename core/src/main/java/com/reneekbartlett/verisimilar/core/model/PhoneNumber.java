package com.reneekbartlett.verisimilar.core.model;

/***
 * North American Numbering Plan Administrator (NANPA) handles all official phone number assignments 
 * for the US, Canada, and various Caribbean countries. They provide free, regularly updated raw datasets
 * 
 * For Area Codes (NPA): You can download the complete NANPA NPA Database CSV which lists all active, 
 * planned, geographic, and non-geographic area codes.
 * 
 * For Exchange Codes (NXX): You can download zipped, tab-delimited text files containing every single assigned 
 * or available prefix sorted by state via the NANPA Central Office Code Assignment Records.
 */
public record PhoneNumber(String areaCode, String exchangeCode, String lineNumber, PhoneNumberType phoneNumberType){

    public PhoneNumber(String areaCode, String exchangeCode, String lineNumber) {
        this(areaCode, exchangeCode, lineNumber, PhoneNumberType.UNKNOWN);
    }
 
    public static PhoneNumber empty() {
        return new PhoneNumber(null, null, null, PhoneNumberType.EMPTY);
    }

    public static PhoneNumber placeholder() {
        return new PhoneNumber("617", "536", "9000", PhoneNumberType.UNKNOWN);
    }

    @Override
    public String toString() {
        final String VALUE_DELIM = "-";
        return new StringBuilder()
            .append(this.areaCode).append(VALUE_DELIM)
            .append(this.exchangeCode).append(VALUE_DELIM)
            .append(this.lineNumber)
            .toString();
    }
}
