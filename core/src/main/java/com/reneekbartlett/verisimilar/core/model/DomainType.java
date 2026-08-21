package com.reneekbartlett.verisimilar.core.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public enum DomainType implements WeightedEnumData {

    B2C("B2C", 0.5000),
    EDU("EDU", 0.0250),
    GOV("GOV", 0.0250),
    DISPOSABLE("DISPOSABLE", 0.0250),
    B2B("B2B", 0.0250);

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

    public static DomainType fromValue(String value) {
        if (value == null) return null;
        for (DomainType domainType : EnumSet.allOf(DomainType.class)) {
            if (domainType.getLabel().equals(value.toUpperCase())) {
                return domainType;
            }
        }
        return null;
    }

    public static Set<DomainType> fromValues(Set<String> values) {
        if (values == null || values.isEmpty()) return null;

        EnumSet<DomainType> domainTypes = EnumSet.noneOf(DomainType.class);
        for (String value : values) {
            try {
                // Assumes DomainType.valueOf(String) matches your label
                domainTypes.add(DomainType.valueOf(value.toUpperCase()));
            } catch (IllegalArgumentException e) {
                // Skip values that don't match any enum constant
                System.out.println("");
            }
        }
        return domainTypes.isEmpty() ? null : domainTypes;
    }

    // TODO:  Get from config, add DomainType.DISPOSABLE
    public static EnumSet<DomainType> defaultDatasets(){
        return EnumSet.of(DomainType.B2C, DomainType.EDU, DomainType.GOV, DomainType.B2B, DomainType.DISPOSABLE);
    }

    public static Map<DomainType, Double> defaultMap() {
        final Map<DomainType, Double> defaultMap = HashMap.newHashMap(4);
        defaultDatasets().forEach(domainType -> {
            defaultMap.put(domainType, domainType.getWeight());
        });
        return defaultMap;
    }

    public static Set<String> labels(Set<DomainType> values) {
        return values.stream()
            .map(DomainType::getLabel)
            .filter(label -> !label.isBlank())
            .collect(Collectors.toSet());
    }

    public static DomainType fromText(String textVal) {
        if(textVal != null) {
            for (DomainType t : EnumSet.allOf(DomainType.class)) {
                if (t.getPlaceholder().equalsIgnoreCase(textVal)) {
                    return t;
                }
            }
        }
        return null;
    }

    public static EnumSet<DomainType> convertToEnumSet(Set<String> stringVals){
        EnumSet<DomainType> typeEnumSet = EnumSet.noneOf(DomainType.class);
        for(String typeStr : stringVals) {
            DomainType t = DomainType.fromText(typeStr);
            if(t != null) {
                typeEnumSet.add(t);
            }
        }
        return typeEnumSet;
    }
}
