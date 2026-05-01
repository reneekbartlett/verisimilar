package com.reneekbartlett.verisimilar.core.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum DomainType {

    B2C("B2C", 0.5000),
    EDU("EDU", 0.0250),
    GOV("GOV", 0.0250),
    DISPOSABLE("DISPOSABLE", 0.0000),
    B2B("B2B", 0.0000);

    private final String label;
    private final double weight;

    private DomainType(String label, double weight) {
        this.label = label;
        this.weight = weight;
    }

    public String getLabel() {
        return label;
    }

    public double getWeight() {
        return weight;
    }

    public String getPlaceholder() {
        return label;
    }

    public static EnumSet<DomainType> defaultDatasets(){
        return EnumSet.of(DomainType.B2C, DomainType.EDU, DomainType.GOV
                //, DomainType.B2B, DomainType.DISPOSABLE
                );
    }

    public static Map<DomainType, Double> defaultMap() {
        final Map<DomainType, Double> defaultMap = HashMap.newHashMap(4);
        defaultDatasets().forEach(domainType -> {
            defaultMap.put(domainType, domainType.getWeight());
        });
        return defaultMap;
    }
}
