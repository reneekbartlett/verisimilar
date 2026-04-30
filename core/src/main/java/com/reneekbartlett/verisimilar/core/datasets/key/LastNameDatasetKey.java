package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.Set;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;

/***
 * Ethnicity
 */
public record LastNameDatasetKey(String id, Set<Ethnicity> ethnicities) implements DatasetKey {

    public static final String KEY_ID = "LASTNAME";

    public LastNameDatasetKey(Set<Ethnicity> ethnicities) {
        this(KEY_ID, ethnicities);
    }

    public LastNameDatasetKey() {
        this(KEY_ID, Set.of(Ethnicity.UNKNOWN));
    }

    public LastNameDatasetKey(Ethnicity ethnicity) {
        this(KEY_ID, Set.of(ethnicity));
    }

    public static LastNameDatasetKey defaults() {
        return new LastNameDatasetKey(KEY_ID, Set.of(Ethnicity.UNKNOWN));
    }

    public static LastNameDatasetKey fromContext(DatasetResolutionContext ctx) {
        Set<Ethnicity> ethnicities = ctx.ethnicities().orElse(Ethnicity.defaultDatasets());
        return new LastNameDatasetKey(KEY_ID, ethnicities);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0).append(id);
        if(ethnicities != null) {
            sb.append("$").append(ethnicities.stream().map(Ethnicity::getPlaceholder).collect(Collectors.joining("|")));
        }
        return sb.toString();
    }
}
