package com.reneekbartlett.verisimilar.core.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum Decade {

    FOURTIES(1940, 1949, 0.0000, "1940"),
    FIFTIES(1950, 1959, 0.0000, "1950"),
    SIXTIES(1960, 1969, 0.0000, "1960"),
    SEVENTIES(1970, 1979, 0.0000, "1970"),
    EIGHTIES(1980, 1989, 0.0000, "1980"),
    NINETIES(1990, 1999, 0.0000, "1990"),
    TWO_THOUSANDS(2000, 2009, 0.0000, "2000"),
    TWO_THOUSAND_TENS(2010, 2019, 0.0000, "2010"),
    UNKNOWN(1940, 2026, 0.0000, "ALL");

    private final Integer startYear;
    private final Integer endYear;
    private final double weight;
    private final String placeholder;

    private Decade(Integer startYear, Integer endYear, double weight, String placeholder) {
        this.startYear = startYear;
        this.endYear = endYear;
        this.weight = weight;
        this.placeholder = placeholder;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public double getWeight() {
        return weight;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public static Set<Decade> defaultDatasets(){
        return Set.of(UNKNOWN);
    }

    public static Map<Decade, Double> defaultMap() {
        Map<Decade, Double> defaultMap = HashMap.newHashMap(0);
        for(Decade decade : defaultDatasets()) {
            defaultMap.put(decade, decade.getWeight());
        }
        return defaultMap;
    }
}
