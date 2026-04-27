package com.reneekbartlett.verisimilar.core.datasets.key;

import com.reneekbartlett.verisimilar.core.model.USState;

public record StreetNameDatasetKey(
        String id, 
        USState state
) implements DatasetKey {

    public static final String KEY_ID = "STREETNAME";

    public static StreetNameDatasetKey defaults() {
        return new StreetNameDatasetKey(KEY_ID, null);
    }

    public StreetNameDatasetKey(USState state) {
        this(KEY_ID, state);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0).append(id);
        if(state != null) sb.append("$").append(state.name());
        return sb.toString();
    }
}
