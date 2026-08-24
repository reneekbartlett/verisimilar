package com.reneekbartlett.verisimilar.core.model;

import java.util.EnumSet;

public enum FilterOperator {
    STARTS_WITH("startswith", true),
    ENDS_WITH("endswith", true),
    CONTAINS("contains", true),
    EQUAL_TO("eq", true),
    IN("in", true);

    private final String keyword;
    private final boolean isEnabled;
    private FilterOperator(String keyword, boolean isEnabled) {
        this.keyword = keyword;
        this.isEnabled = isEnabled;
    }

    public String keyword() {
        return this.keyword;
    }

    public boolean isEnabled() {
        return this.isEnabled;
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
