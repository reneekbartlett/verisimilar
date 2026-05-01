package com.reneekbartlett.verisimilar.core.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Ethnicity {

    HISPANIC_OR_LATINO("Hispanic or Latino", 0.0000, ""),
    NOT_HISPANIC_OR_LATINO("Not Hispanic or Latino", 0.0000, ""),

    // These are official "race" categories from US Census Bureau.
    WHITE("White", 0.0000, ""),
    BLACK_OR_AFRICAN_AMERICAN("Black or African American", 0.0000, ""),
    AMERICAN_INDIAN_OR_ALASKA_NATIVE("American Indian or Alaska Native", 0.0000, ""),
    ASIAN("Asian", 0.0000, ""),
    NATIVE_HAWAIIAN_OR_PACIFIC_ISLANDER("Native Hawaiian or Other Pacific Islander", 0.0000, ""),

    TWO_OR_MORE_RACES("Two or More Races", 0.0000, ""),
    SOME_OTHER_RACE("Some Other Race", 0.0000, ""),

    INDIAN("Indian", 0.0000, "india"),
    CHINESE("Chinese", 0.0000, "china"),

    UNKNOWN("", 0.0000, "ALL"),
    DECLINE_TO_ANSWER("Decline to Answer", 0.0000, "ALL");

    private final String label;
    private final double weight;
    private final String placeholder;

    private Ethnicity(String label, double weight, String placeholder) {
        this.label = label;
        this.weight = weight;
        this.placeholder = placeholder;
    }

    public String getLabel() {
        return label;
    }

    public double getWeight() {
        return weight;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public static EnumSet<Ethnicity> defaultDatasets(){
        return EnumSet.of(UNKNOWN, INDIAN);
    }

    public static Map<Ethnicity, Double> defaultMap() {
        Map<Ethnicity, Double> defaultMap = HashMap.newHashMap(0);
        for(Ethnicity ethnicity : defaultDatasets()) {
            defaultMap.put(ethnicity, 0.5000); // TODO:  use ethnicity.getWeight()
        }
        return defaultMap;
    }
}
