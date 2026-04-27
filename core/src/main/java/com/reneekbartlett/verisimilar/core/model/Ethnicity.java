package com.reneekbartlett.verisimilar.core.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum Ethnicity {

    HISPANIC_OR_LATINO("Hispanic or Latino", 0.0000),
    NOT_HISPANIC_OR_LATINO("Not Hispanic or Latino", 0.0000),

    // These are official "race" categories from US Census Bureau.
    WHITE("White", 0.0000),
    BLACK_OR_AFRICAN_AMERICAN("Black or African American", 0.0000),
    AMERICAN_INDIAN_OR_ALASKA_NATIVE("American Indian or Alaska Native", 0.0000),
    ASIAN("Asian", 0.0000),
    NATIVE_HAWAIIAN_OR_PACIFIC_ISLANDER("Native Hawaiian or Other Pacific Islander", 0.0000),

    TWO_OR_MORE_RACES("Two or More Races", 0.0000),
    SOME_OTHER_RACE("Some Other Race", 0.0000),

    INDIAN("Indian", 0.0000),
    CHINESE("Chinese", 0.0000),

    UNKNOWN("", 0.0000),
    DECLINE_TO_ANSWER("Decline to Answer", 0.0000);

    private final String label;
    private final double weight;

    private Ethnicity(String label, double weight) {
        this.label = label;
        this.weight = weight;
    }

    public String getLabel() {
        return label;
    }

    public double getWeight() {
        return weight;
    }

    public static Set<Ethnicity> defaultDatasets(){
        return Set.of(UNKNOWN, CHINESE);
    }

    public static Map<Ethnicity, Double> defaultMap() {
        Map<Ethnicity, Double> defaultMap = HashMap.newHashMap(1);
        defaultMap.put(Ethnicity.UNKNOWN, 0.5000);
        defaultMap.put(Ethnicity.CHINESE, 0.5000);
        //defaultMap.put(Ethnicity.INDIAN, 0.5000);
        return defaultMap;
    }
}
