package com.reneekbartlett.verisimilar.core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum USState {
    AL("Alabama",24,0.003262),
    AK("Alaska",52,0.001399), // TODO:  Fix weight
    AR("Arkansas",33,0.002372),
    AZ("Arizona",14,0.005593),
    CA("California",1,0.078247),
    CO("Colorado",21,0.003726),
    CT("Connecticut",29,0.002682),
    DE("Delaware",45,0.001655),
    FL("Florida",3,0.026082),
    GA("Georgia",8,0.009781),
    HI("Hawaii",40,0.001915),
    IA("Iowa",31,0.002527),
    ID("Idaho",38,0.002033),
    IL("Illinois",6,0.013041),
    IN("Indiana",16,0.004914),
    KS("Kansas",35,0.002229),
    KY("Kentucky",26,0.003007),
    LA("Louisiana",25,0.003130),
    MA("Massachusetts",17,0.004637),
    MD("Maryland",18,0.004391),
    ME("Maine",42,0.001805),
    MI("Michigan",10,0.007825),
    MN("Minnesota",22,0.003557),
    MO("Missouri",19,0.004171),
    MS("Mississippi",34,0.002299),
    MT("Montana",43,0.001753),
    NC("North Carolina",9,0.008694),
    ND("North Dakota",47,0.001564),
    NE("Nebraska",37,0.002096),
    NH("New Hampshire",41,0.001859),
    NJ("New Jersey",11,0.007113),
    NM("New Mexico",36,0.002161),
    NV("Nevada",32,0.002448),
    NY("New York",4,0.019562),
    OH("Ohio",7,0.011178),
    OK("Oklahoma",28,0.002784),
    OR("Oregon",27,0.002892),
    PA("Pennsylvania",5,0.015649),
    RI("Rhode Island",44,0.001703),
    SC("South Carolina",23,0.003403),
    SD("South Dakota",46,0.001609),
    TN("Tennessee",15,0.005230),
    TX("Texas",2,0.039124),
    UT("Utah",30,0.002608),
    VA("Virginia",12,0.006520),
    VT("Vermont",49,0.001479),
    WA("Washington",13,0.006019),
    WI("Wisconsin",20,0.003912),
    WV("West Virginia",39,0.001973),
    WY("Wyoming",50,0.001438),
    DC("District of Columbia",51,0.001399);

    private final String fullName;
    private final int rank;
    private final double weight;

    private USState(String fullName, int rank, double weight) {
        this.fullName = fullName;
        this.rank = rank;
        this.weight = weight;
    }

    public String getFullName() {
        return fullName;
    }

    public int getRank() {
        return rank;
    }

    public double getWeight() {
        return weight;
    }

    public static USState fromAbbreviation(String abbr) {
        if(abbr != null) {
            for (USState s : values()) {
                if (s.name().equalsIgnoreCase(abbr)) {
                    return s;
                }
            }
        }
        //throw new IllegalArgumentException("Invalid state abbreviation: " + abbr);
        return null;
    }

    public static Set<USState> all() {
        Set<USState> allStates = new HashSet<>();
        for(USState s : USState.values()){
            allStates.add(s);
        }
        return allStates;
    }

    public static Set<String> names(Set<USState> states) {
        Set<String> stateNames = new HashSet<>();
        for(USState state : states) {
            stateNames.add(state.name());
        }
        return stateNames;
    }

    public static Map<USState, Double> defaultMap() {
        Map<USState, Double> defaultMap = new HashMap<>();
        for(USState state : USState.values()) {
            defaultMap.put(state, state.getWeight());
        }
        return defaultMap;
    }

    public static Map<String, Double> toWeightedMap(Set<String> states) {
        Map<String, Double> weightedMap = new HashMap<>();
        for(String stateAbbr : states) {
            USState state = USState.fromAbbreviation(stateAbbr);
            double stateWeight = state == null ? 0.0001 : state.weight;
            weightedMap.put(stateAbbr, stateWeight);
        }
        return weightedMap;
    }
}
