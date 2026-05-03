package com.reneekbartlett.verisimilar.core.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum PhoneNumberType {
    MOBILE("mobile", 0.9000),
    HOME("home", 0.0100);

    /***
     * Placeholder used in the StringTemplate
     */
    private final String label;
    private final double weight;

    private PhoneNumberType(String label, double weight) {
        this.label = label;
        this.weight = weight;
    }

    public String getPlaceholder() {
        return this.label;
    }

    public String getLabel() {
        return this.label;
    }

    public static PhoneNumberType fromValue(String templateValue) {
        if(templateValue != null) {
            for (PhoneNumberType type : EnumSet.allOf(PhoneNumberType.class)) {
                if (type.name().equalsIgnoreCase(templateValue)) {
                    return type;
                }
            }
        }
        //throw new IllegalArgumentException("Invalid state abbreviation: " + abbr);
        return null;
    }

    public static EnumSet<PhoneNumberType> defaultDatasets(){
        return EnumSet.of(
                PhoneNumberType.MOBILE, PhoneNumberType.HOME
        );
    }

    public static Map<PhoneNumberType, Double> defaultMap() {
        final Map<PhoneNumberType, Double> defaultMap = HashMap.newHashMap(2);
        defaultDatasets().forEach(phoneNumberType -> {
            defaultMap.put(phoneNumberType, phoneNumberType.weight);
        });
        return defaultMap;
    }
}
