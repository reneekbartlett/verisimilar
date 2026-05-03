package com.reneekbartlett.verisimilar.core.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum GenderIdentity {
    MALE("MALE", 1, 0.4995, "male"),
    FEMALE("FEMALE", 2, 0.4995, "female"),
    NONBINARY("NON-BINARY", 3, 0.0010, "unisex"),
    GENDER_UNSPECIFIED("UNSPECIFIED", 0, 0.0000, "unisex");

    private final String label;
    private final int value;
    private final double weight;
    private final String placeholder;

    private GenderIdentity(String label, int value, double weight, String placeholder) {
        this.label = label;
        this.value = value;
        this.weight = weight;
        this.placeholder = placeholder;
    }

    public String getLabel() {
        return label;
    }

    public int getValue() {
        return value;
    }

    public double getWeight() {
        return this.weight;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public boolean isFemale() {
        return this == FEMALE;
    }

    public boolean isMale() {
        return this == MALE;
    }

    public static GenderIdentity fromValue(int value) {
        for (GenderIdentity gender : EnumSet.allOf(GenderIdentity.class)) {
            if (gender.value == value) {
                return gender;
            }
        }
        //throw new IllegalArgumentException("Unknown gender identity value: " + value);
        return null;
    }

    public static GenderIdentity fromText(String text) {
        if(text != null) {
            for (GenderIdentity g : values()) {
                if (g.name().equalsIgnoreCase(text)) {
                    return g;
                } else if (text.equalsIgnoreCase("F")) {
                    return GenderIdentity.FEMALE;
                } else if (text.equalsIgnoreCase("M")) {
                    return GenderIdentity.MALE;
                }
            }
        }
        return null;
    }

    public static EnumSet<GenderIdentity> defaultDatasets(){
        return EnumSet.of(MALE, FEMALE);
    }

    public static EnumSet<GenderIdentity> defaults() {
        return EnumSet.of(MALE, FEMALE);
    }

    public static Map<GenderIdentity, Double> defaultMap() {
        Map<GenderIdentity, Double> defaultMap = HashMap.newHashMap(0);
        for(GenderIdentity genderIdentity : defaultDatasets()) {
            defaultMap.put(genderIdentity, 0.5000); // TODO:  use genderIdentity.getWeight()
        }
        return defaultMap;
    }

}
