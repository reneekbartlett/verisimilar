package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.Set;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.model.Decade;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;

/***
 * GenderIdentity gender
 * Integer year
 * TODO: Generation
 * TODO: Regions/States/Location
 */
public record MiddleNameDatasetKey(
        String id,
        Set<GenderIdentity> genders,
        Set<Decade> decades
) implements DatasetKey {

    public static final String KEY_ID = "MIDDLENAME";

    public static MiddleNameDatasetKey defaults() {
        return new MiddleNameDatasetKey(KEY_ID, GenderIdentity.defaultDatasets(), Decade.defaultDatasets());
    }

    public MiddleNameDatasetKey(Set<GenderIdentity> genders) {
        this(KEY_ID, genders, Decade.defaultDatasets());
    }

    public MiddleNameDatasetKey(Set<GenderIdentity> genders, Set<Decade> decades) {
        this(KEY_ID, genders, decades);
    }

    public static MiddleNameDatasetKey fromContext(DatasetResolutionContext ctx) {
        Set<GenderIdentity> genders = ctx.genders().orElse(GenderIdentity.defaultDatasets());
        //Set<Ethnicity> ethnicities = ctx.ethnicities().orElse(Ethnicity.defaultDatasets());
        Set<Decade> decades = ctx.decades().orElse(Decade.defaultDatasets());
        return new MiddleNameDatasetKey(KEY_ID, genders, decades);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0).append(id);
        //if(gender != null) sb.append("$").append(gender.getLabel());
        if(genders != null) sb.append("$").append(genders.stream().map(Enum::name).collect(Collectors.joining("|")));
        if(decades != null) sb.append("$").append(decades.stream().map(Enum::name).collect(Collectors.joining("|")));
        return sb.toString();
    }
}
