package com.reneekbartlett.verisimilar.core.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public record CityStateZip(String city, String state, String zip){

    public static CityStateZip empty() {
        return new CityStateZip(null, null, null);
    }

    public static CityStateZip placeholder() {
        return new CityStateZip("BOSTON", "MA", "02116");
    }

    @Override
    public String toString() {
        final String VALUE_DELIM = " ";
        return new StringBuilder()
            .append(this.city).append(VALUE_DELIM)
            .append(this.state).append(VALUE_DELIM)
            .append(this.zip).append(VALUE_DELIM)
            .toString().toUpperCase();
    }

    public static Map<String, Double> toWeightedMap(Set<String> cityStateZips) {
        Map<String, Double> weightedMap = new HashMap<>();
        for(String val : cityStateZips) {
            weightedMap.put(val, 0.0001);
        }
        return weightedMap;
    }

    public static Map<String, Double> toWeightedMap(String[] cityStateZips) {
        Map<String, Double> weightedMap = new HashMap<>();
        for(String val : cityStateZips) {
            weightedMap.put(val, 0.0001);
        }
        return weightedMap;
    }
}
