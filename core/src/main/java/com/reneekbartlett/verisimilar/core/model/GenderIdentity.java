package com.reneekbartlett.verisimilar.core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum GenderIdentity {
    MALE("MALE", 1, 0.4995),
    FEMALE("FEMALE", 2, 0.4995),
    NONBINARY("NON-BINARY", 3, 0.0010),
    GENDER_UNSPECIFIED("UNSPECIFIED", 0, 0.0000);

    private final String label;
    private final int value;
    private final double weight;

    private GenderIdentity(String label, int value, double weight) {
        this.label = label;
        this.value = value;
        this.weight = weight;
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

    public boolean isFemale() {
        return this == FEMALE;
    }

    public boolean isMale() {
        return this == MALE;
    }

    public static GenderIdentity fromValue(int value) {
        for (GenderIdentity gender : GenderIdentity.values()) {
            if (gender.value == value) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Unknown gender identity value: " + value);
    }

    public static Set<GenderIdentity> defaults() {
        Set<GenderIdentity> defaults = new HashSet<>();
        defaults.add(MALE);
        defaults.add(FEMALE);
        //defaults.add(NONBINARY);
        return defaults;
    }

    public static Map<GenderIdentity, Double> defaultMap() {
        Map<GenderIdentity, Double> defaultMap = HashMap.newHashMap(3);
        defaultMap.put(GenderIdentity.MALE, GenderIdentity.MALE.getWeight());
        defaultMap.put(GenderIdentity.FEMALE, GenderIdentity.FEMALE.getWeight());
        //defaultMap.put(GenderIdentity.NONBINARY, GenderIdentity.NONBINARY.getWeight());
        return defaultMap;
    }

}
