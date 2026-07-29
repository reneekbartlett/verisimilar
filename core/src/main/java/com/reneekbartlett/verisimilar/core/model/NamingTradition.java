package com.reneekbartlett.verisimilar.core.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum NamingTradition implements WeightedEnumData {

    IRISH("IRISH", 0.0000, ""),

    // Mateo, Sofia, Santiago, Valentina, Sebastian, Camila, Diego, Isabella
    HISPANIC_LATINO("HISPANIC_LATINO", 0.0000, ""),
    
    RELIGIOUS("RELIGIOUS", 0.0000, ""),
    ROMANCE("ROMANCE", 0.0000, ""), // Covers Italian, French, Spanish, Portuguese, etc.

    ASIAN("ASIAN", 0.0000, ""),
    CHINESE("CHINESE", 0.0000, ""),
    EAST_ASIAN("EAST_ASIAN", 0.0000, ""),
    SOUTH_ASIAN("SOUTH_ASIAN", 0.0000, ""),

    // Navajo, Dakota, Cherokee, Choctaw, Iroquois, Sioux, etc.
    AMERICAN_NATIVE("AMERICAN_NATIVE", 0.0000, ""),

    GERMANIC("GERMANIC", 0.0000, ""),

    SANSKRITIC("SANSKRITIC", 0.0000, ""),

    AFRICAN_AMERICAN("AFRICAN_AMERICAN", 0.0000, ""),
    ARABIC("ARABIC", 0.0000, ""),
    SLAVIC("SLAVIC", 0.0000, ""),

    PATRONYMIC("PATRONYMIC", 0.0000, ""),
    TOPONYMIC("TOPONYMIC", 0.0000, ""),

    GENERIC("", 0.0000, "ALL"),
    UNKNOWN("", 0.0000, "ALL");

    private final String label;
    private final double weight;
    private final String placeholder;

    private NamingTradition(String label, double weight, String placeholder) {
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

    public static EnumSet<NamingTradition> defaultDatasets(){
        return EnumSet.of(UNKNOWN);
    }

    public static Map<NamingTradition, Double> defaultMap() {
        Map<NamingTradition, Double> defaultMap = HashMap.newHashMap(0);
        for(NamingTradition namingTradition : defaultDatasets()) {
            defaultMap.put(namingTradition, 0.5000); // TODO:  use .getWeight()
        }
        return defaultMap;
    }

    public static NamingTradition fromText(String textVal) {
        if(textVal != null) {
            for (NamingTradition namingTradition : EnumSet.allOf(NamingTradition.class)) {
                if (namingTradition.name().equalsIgnoreCase(textVal) || namingTradition.getPlaceholder().equalsIgnoreCase(textVal)) {
                    return namingTradition;
                }
            }
        }
        return null;
    }

    public static EnumSet<NamingTradition> convertToEnumSet(Set<String> namingTraditions){
        EnumSet<NamingTradition> namingTraditionEnumSet = EnumSet.noneOf(NamingTradition.class);
        for(String namingTraditionStr : namingTraditions) {
            NamingTradition namingTradition = NamingTradition.fromText(namingTraditionStr);
            if(namingTradition != null) {
                namingTraditionEnumSet.add(namingTradition);
            }
        }
        return namingTraditionEnumSet;
    }
}
