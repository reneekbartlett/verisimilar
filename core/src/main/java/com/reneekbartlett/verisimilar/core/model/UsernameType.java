package com.reneekbartlett.verisimilar.core.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum UsernameType {

    KEYWORD1("KEYWORD1", 0.5000, "ALL"),
    KEYWORD2("KEYWORD2", 0.5000, "XTRA");

    private final String label;
    private final double weight;
    private final String placeholder;

    private UsernameType(String label, double weight, String placeholder) {
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

    public static EnumSet<UsernameType> defaultDatasets(){
        return EnumSet.of(
                UsernameType.KEYWORD1
                //,UsernameType.KEYWORD2
        );
    }

    public static Map<UsernameType, Double> defaultMap() {
        final Map<UsernameType, Double> defaultMap = HashMap.newHashMap(2);
        defaultDatasets().forEach(usernameType -> {
            defaultMap.put(usernameType, usernameType.getWeight());
        });
        return defaultMap;
    }
}
