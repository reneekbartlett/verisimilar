package com.reneekbartlett.verisimilar.core.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum KeywordType {

    KEYWORD1("KEYWORD1", 0.5000, "ALL"),
    KEYWORD2("KEYWORD2", 0.0250, "XTRA");

    private final String label;
    private final double weight;
    private final String placeholder;

    private KeywordType(String label, double weight, String placeholder) {
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

    public static Set<KeywordType> defaultDatasets(){
        return Set.of(KeywordType.KEYWORD1);
    }

    public static Map<KeywordType, Double> defaultMap() {
        final Map<KeywordType, Double> defaultMap = HashMap.newHashMap(1);
        defaultDatasets().forEach(keywordType -> {
            defaultMap.put(keywordType, keywordType.getWeight());
        });
        return defaultMap;
    }
}
