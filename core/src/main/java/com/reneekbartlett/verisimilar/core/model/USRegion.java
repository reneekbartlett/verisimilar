package com.reneekbartlett.verisimilar.core.model;

import java.util.Set;

// TODO: Implement
public enum USRegion {
    // US Census
    NORTHEAST("Northeast", 4, 0.120000, "CT", "ME", "MA", "NH", "RI", "VT", "NJ", "NY", "PA"),
    MIDWEST("Midwest", 3, 0.160000, "IL", "IN", "MI", "OH", "WI", "IA", "KS", "MN", "MO", "NE", "ND", "SD"),
    SOUTH("South", 1, 0.480000, "DE", "FL", "GA", "MD", "NC", "SC", "VA", "WV", "AL", "KY", "MS", "TN", "AR", "LA", "OK", "TX", "DC"),
    WEST("West", 2, 0.240000, "AZ", "CO", "ID", "MT", "NV", "NM", "UT", "WY"),
    // TODO.. fix rank/weight
    PACIFIC("Pacific", 5, 0.110000, "AK", "CA", "HI", "OR", "WA");

    // MID-ATLANTIC  DC
    // SOUTH-ATLANTIC  DE

    // EAST_COAST
    // WEST_COAST

    private final String regionName;
    private final int rank;
    private final double weight;
    private final Set<String> states;

    private USRegion(String regionName, int rank, double weight, String... states) {
        this.regionName = regionName;
        this.rank = rank;
        this.weight = weight;
        this.states = Set.of(states);
    }

    public String getRegionName() {
        return regionName;
    }

    public int getRank() {
        return rank;
    }

    public double getWeight() {
        return weight;
    }

    public Set<String> getStates() {
        return states;
    }

    public static USRegion getRegionForState(String stateAbbreviation) {
        for (USRegion region : USRegion.values()) {
            if (region.states.contains(stateAbbreviation.toUpperCase())) {
                return region;
            }
        }
        throw new IllegalArgumentException("Invalid state abbreviation: " + stateAbbreviation);
    }
}
