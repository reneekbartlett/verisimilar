package com.reneekbartlett.verisimilar.core.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/***
 * 1) Predirectional "N BAY ST" 
 * 2) Postdirectional "BAY DR W"
 * 3) As part of Streetname [Preferred: BAY WEST DR, NORTH AVE|Acceptable: BAY W DRIVE, NORTH AVENUE]
 * 4) As suffix
 */
public enum Directional implements WeightedEnumData {
    // Predirectional or Postdirectional
    NORTH("N", 0.0000, "NORTH", "N"),
    SOUTH("S", 0.0000, "SOUTH", "S"),
    EAST("E", 0.0000, "EAST", "E"),
    WEST("W", 0.0000, "WEST", "W"),

    // Two Directionals
    NORTHEAST("N E", 0.0000, "NORTHEAST", "N E", "N EAST", "NE"),
    NORTHWEST("N W", 0.0000, "NORTHWEST", "N W", "N WEST", "NW"),
    SOUTHEAST("S E", 0.0000, "SOUTHEAST", "S E", "S EAST", "SE"),
    SOUTHWEST("S W", 0.0000, "SOUTHWEST", "S W", "S WEST", "SW"),

    NONE("", 0.0000, " ");

    private final String label;
    private final double weight;
    private final String[] searchStr;

    private Directional(String label, double weight, String... searchStr) {
        this.label = label; // usps standard
        this.weight = weight;
        this.searchStr = searchStr;
    }

    public String getLabel() {
        return label;
    }

    public double getWeight() {
        return weight;
    }

    public String[] getSearchStrings() {
        return searchStr;
    }

    public static EnumSet<Directional> defaultDatasets(){
        return EnumSet.allOf(Directional.class);
    }

    public static Map<Directional, Double> defaultMap() {
        Map<Directional, Double> defaultMap = HashMap.newHashMap(defaultDatasets().size());
        for(Directional directional : defaultDatasets()) {
            defaultMap.put(directional, directional.getWeight());
        }
        return defaultMap;
    }

    public static Directional fromLabel(String label) {
        for (Directional directional : EnumSet.allOf(Directional.class)) {
            if (directional.name().equalsIgnoreCase(label) || directional.getLabel().equalsIgnoreCase(label)) {
                return directional;
            }
        }
        return null;
    }

    public static EnumSet<Directional> convertToEnumSet(Set<String> directionals){
        EnumSet<Directional> directionalEnumSet = EnumSet.noneOf(Directional.class);
        for(String directional : directionals) {
            Directional dir = Directional.fromLabel(directional);
            if(dir != null) {
                directionalEnumSet.add(dir);
            }
        }
        return directionalEnumSet;
    }

    public static EnumSet<Directional> getWeightedEnumDataSet() {
        return defaultDatasets();
    }
}
