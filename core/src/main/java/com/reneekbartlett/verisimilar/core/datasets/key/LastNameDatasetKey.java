package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.EnumSet;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;

/***
 * Ethnicity
 */
public record LastNameDatasetKey(String id, EnumSet<Ethnicity> ethnicities) implements DatasetKey {

    public static final String KEY_ID = "LASTNAME";

    public LastNameDatasetKey(EnumSet<Ethnicity> ethnicities) {
        this(KEY_ID, ethnicities);
    }

    public LastNameDatasetKey() {
        this(KEY_ID, EnumSet.of(Ethnicity.UNKNOWN));
    }

    public LastNameDatasetKey(Ethnicity ethnicity) {
        this(KEY_ID, EnumSet.of(ethnicity));
    }

    public static LastNameDatasetKey defaults() {
        return new LastNameDatasetKey(KEY_ID, EnumSet.of(Ethnicity.UNKNOWN));
    }

    public static LastNameDatasetKey fromContext(DatasetResolutionContext ctx) {
        EnumSet<Ethnicity> ethnicities = ctx.ethnicities().orElse(Ethnicity.defaultDatasets());
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
