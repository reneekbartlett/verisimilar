package com.reneekbartlett.verisimilar.core.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum UsernameType {

    KEYWORD1("KEYWORD1", 0.5000),
    KEYWORD2("KEYWORD2", 0.5000);

    private final String label;
    private final double weight;

    private UsernameType(String label, double weight) {
        this.label = label;
        this.weight = weight;
    }

    public String getLabel() {
        return label;
    }

    public double getWeight() {
        return weight;
    }

    public static Set<UsernameType> defaultDatasets(){
        return Set.of(
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
