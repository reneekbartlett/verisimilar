package com.reneekbartlett.verisimilar.core.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// TODO: Rename to US_CENSUS_?

/***
 * These are demographic classifications based on US Census criteria.
 * https://www.census.gov/newsroom/press-releases/2026/2020-census-names-data.html
 * https://www2.census.gov/library/publications/decennial/2020/c2020br-13.pdf
 * https://www.census.gov/library/stories/2026/07/foreign-born-population-250-year-history.html
 * https://www.census.gov/library/stories/2026/04/2020-census-names-data.html
 * 
 * The SSA counts the names of the babies born in 2020, while the Census Bureau counts the names of all respondents.
 */
public enum Ethnicity implements WeightedEnumData {

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

    GENERIC("", 0.0000, "ALL"),
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

    public static Ethnicity fromText(String textVal) {
        if(textVal != null) {
            for (Ethnicity eth : EnumSet.allOf(Ethnicity.class)) {
                if (eth.name().equalsIgnoreCase(textVal) || eth.getPlaceholder().equalsIgnoreCase(textVal)) {
                    return eth;
                }
            }
        }
        return null;
    }

    public static EnumSet<Ethnicity> convertToEnumSet(Set<String> ethnicities){
        EnumSet<Ethnicity> ethEnumSet = EnumSet.noneOf(Ethnicity.class);
        for(String ethStr : ethnicities) {
            Ethnicity eth = Ethnicity.fromText(ethStr);
            if(eth != null) {
                ethEnumSet.add(eth);
            }
        }
        return ethEnumSet;
    }
}
