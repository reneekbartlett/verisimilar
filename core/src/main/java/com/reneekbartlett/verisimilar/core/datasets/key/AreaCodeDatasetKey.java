package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.Set;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;

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

    public static AreaCodeDatasetKey fromContext(DatasetResolutionContext ctx) {
        Set<USState> states = ctx.states().orElse(USState.defaultDatasets());
        return new AreaCodeDatasetKey(KEY_ID, states);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0).append(id);
        if(states != null) sb.append("$").append(states.stream().map(USState::getPlaceholder).collect(Collectors.joining("|")));
        return sb.toString();
    }
}
