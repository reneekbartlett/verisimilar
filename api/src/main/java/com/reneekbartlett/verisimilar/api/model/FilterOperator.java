package com.reneekbartlett.verisimilar.api.model;

import java.util.EnumSet;

public enum FilterOperator {
    STARTS_WITH("startswith"),
    ENDS_WITH("endswith"),
    EQUAL_TO("eq"),
    IN("in");

    private final String keyword;
    private FilterOperator(String keyword) {
        this.keyword = keyword;
    }

    public String keyword() {
        return this.keyword;
    }

    public static FilterOperator fromKeyword(String value) {
        if(value != null) {
            for (FilterOperator operator : EnumSet.allOf(FilterOperator.class)) {
                if (operator.keyword().equalsIgnoreCase(value) || operator.name().equalsIgnoreCase(value)) {
                    return operator;
                }
            }
        }
        return null;
    }
}
