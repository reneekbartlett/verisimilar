package com.reneekbartlett.verisimilar.core.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public enum Generation {
    LOST_GENERATION("Lost Generation", false, 1883, 1900, 0.0010),
    GREATEST_GENERATION("Greatest Generation", false , 1901, 1927, 0.0010),
    BUILDERS("Builders", false, 1928, 1945, 0.0020),
    BABY_BOOMER("Baby Boomer", false, 1946, 1964, 0.0050),
    GENERATION_X("Generation X", false, 1965, 1980, 0.0100),
    MILLENNIAL("Millennial", false, 1981, 1996, 0.3000),
    GENERATION_Z("Generation Z", false, 1997, 2012, 0.3000),
    GENERATION_ALPHA("Generation Alpha", false, 2013, 2025, 0.0000),

    LEADING_EDGE_BOOMER("Leading Edge Boomer", true, 1946, 1955, 0.0000),
    GENERATION_JONES("Generation Jones", true, 1956, 1964, 0.0000),
    XENNIAL("Xennial", true, 1977, 1985, 0.0000),
    BABY_BUST("baby_bust", true, 1960, 1980, 0.0000),
    MIDDLE_CHILD("middle_child", true, 1965, 1980, 0.0000),
    GERIATRIC_MILLENNIAL("geriatric_millennial", true, 1981, 1987, 0.0000),
    CUSPER("cusper", true, 1994, 2000, 0.0000),
    ZILLENNIAL("zillennial", true, 1993, 1998, 0.0000),
    GEN_Z_1("gen_z_1", true, 1995, 2002, 0.0000),
    GEN_Z_2("gen_z_2", true, 2003, 2012, 0.0000),

    UNKNOWN("Unknown", false, 0, 0, 1.0000);

    private final String displayName;
    private final boolean isMicro;
    private final int startYear;
    private final int endYear;
    private final double weight;

    private Generation(String displayName, boolean isMicro, int startYear, int endYear, double weight) {
        this.displayName = displayName;
        this.isMicro = isMicro;
        this.startYear = startYear;
        this.endYear = endYear;
        this.weight = weight;
    }

    public String getDisplayName() { return displayName; }
    public int getStartYear() { return startYear; }
    public int getEndYear() { return endYear; }
    public double getWeight() { return weight; }

    @Override
    public String toString() {
        return displayName + " (" + getStartYear() + "-" + getEndYear() + ")";
    }

    public static Set<Generation> defaultDatasets(){
        return Set.of(UNKNOWN, BABY_BOOMER, GENERATION_X, MILLENNIAL, GENERATION_Z, GENERATION_ALPHA);
    }

    public static Map<Generation, Double> defaultMap() {
        // TODO:  Make sure weight adds to 1
        Map<Generation, Double> defaultMap = HashMap.newHashMap(defaultDatasets().size());
        for(Generation g : defaultDatasets()) {
            defaultMap.put(g, g.weight);
        }
        return defaultMap;
    }

    // TODO: Return list?
    public static Generation fromYear(int year) {
        for (Generation gen : values()) {
            if (year >= gen.startYear && year <= gen.endYear) {
                return gen;
            }
        }
        throw new IllegalArgumentException("Year out of range");
    }

    public static Generation fromYear(int year, boolean isMicro) {
        for (Generation gen : getByCategory(isMicro)) {
            if (year >= gen.startYear && year <= gen.endYear) {
                return gen;
            }
        }
        throw new IllegalArgumentException("Year out of range");
    }

    public static List<Generation> getByCategory(boolean isMicro) {
        return Arrays.stream(values())
                .filter(s -> s.isMicro == isMicro)
                .collect(Collectors.toList());
    }

    public static boolean validateGeneration(Generation generation) throws IllegalArgumentException {
        if (generation == null || generation.getDisplayName() == Generation.UNKNOWN.getDisplayName()) {
            throw new IllegalArgumentException("Unrecognized Generation value. Provided: " + generation);
        }
        return true;
    }
}
