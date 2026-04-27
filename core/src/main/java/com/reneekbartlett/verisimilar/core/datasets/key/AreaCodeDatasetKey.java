package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.Set;

import com.reneekbartlett.verisimilar.core.model.USState;

public record AreaCodeDatasetKey(String id, Set<USState> states) implements DatasetKey {

    public static final String KEY_ID = "AREACODE";

    public AreaCodeDatasetKey() {
        this(KEY_ID, Set.of(USState.values()));
    }

    public AreaCodeDatasetKey(Set<USState> states) {
        this(KEY_ID, states);
    }

    public static AreaCodeDatasetKey defaults() {
        return new AreaCodeDatasetKey(KEY_ID, Set.of(USState.values()));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0).append(id);
        if(states != null) sb.append("$").append(String.join("$", USState.names(states)));
        return sb.toString();
    }
}
