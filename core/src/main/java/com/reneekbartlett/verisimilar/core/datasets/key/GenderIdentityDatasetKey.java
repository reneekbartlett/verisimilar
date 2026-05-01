package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.EnumSet;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;

public record GenderIdentityDatasetKey(String id, EnumSet<GenderIdentity> genders) implements DatasetKey {

    public static final String KEY_ID = "GENDER_IDENTITY";

    public GenderIdentityDatasetKey(EnumSet<GenderIdentity> genders) {
        this(KEY_ID, genders);
    }

    public GenderIdentityDatasetKey() {
        this(KEY_ID, GenderIdentity.defaultDatasets());
    }

    public static GenderIdentityDatasetKey defaults() {
        return new GenderIdentityDatasetKey(KEY_ID, GenderIdentity.defaultDatasets());
    }

    public static GenderIdentityDatasetKey fromContext(DatasetResolutionContext ctx) {
        EnumSet<GenderIdentity> genders = ctx.genders().orElse(GenderIdentity.defaultDatasets());
        return new GenderIdentityDatasetKey(KEY_ID, genders);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0).append(id);
        if(genders != null) sb.append("$").append(genders.stream().map(GenderIdentity::getPlaceholder).collect(Collectors.joining("|")));
        return sb.toString();
    }
}
