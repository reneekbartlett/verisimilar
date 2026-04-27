package com.reneekbartlett.verisimilar.core.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum DomainType {

    B2C("B2C", 0.5000),
    EDU("EDU", 0.0250),
    GOV("GOV", 0.0250),
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

    public static Set<DomainType> defaultDatasets(){
        return Set.of(DomainType.B2C, DomainType.EDU, DomainType.GOV
                //, DomainType.B2B
                );
    }

    public static Map<DomainType, Double> defaultMap() {
        final Map<DomainType, Double> defaultMap = HashMap.newHashMap(4);
        defaultDatasets().forEach(domainType -> {
            defaultMap.put(domainType, domainType.getWeight());
        });
        //defaultMap.put(DomainType.B2C, 0.5000);
        //defaultMap.put(DomainType.B2B, 0.0020);
        //defaultMap.put(DomainType.EDU, 0.0020);
        //defaultMap.put(DomainType.GOV, 0.0020);
        return defaultMap;
    }
}
