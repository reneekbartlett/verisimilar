package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.EnumSet;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.model.Decade;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;

/***
 * 
 */
public record FirstNameDatasetKey(
        String id,
        EnumSet<GenderIdentity> genders,
        EnumSet<Ethnicity> ethnicities,
        EnumSet<Decade> decades
) implements DatasetKey {

    public static final String KEY_ID = "FIRSTNAME";

    public FirstNameDatasetKey(GenderIdentity gender) {
        this(KEY_ID, EnumSet.of(gender), Ethnicity.defaultDatasets(), Decade.defaultDatasets());
    }

    public FirstNameDatasetKey(EnumSet<GenderIdentity> genders, EnumSet<Ethnicity> ethnicities) {
        this(KEY_ID, genders, ethnicities, Decade.defaultDatasets());
    }

    public FirstNameDatasetKey(EnumSet<Ethnicity> ethnicities) {
        this(KEY_ID, GenderIdentity.defaultDatasets(), ethnicities, Decade.defaultDatasets());
    }

    public static FirstNameDatasetKey defaults() {
        return new FirstNameDatasetKey(KEY_ID, GenderIdentity.defaultDatasets(), Ethnicity.defaultDatasets(), Decade.defaultDatasets());
    }

    public static FirstNameDatasetKey fromContext(DatasetResolutionContext ctx) {
        EnumSet<GenderIdentity> genders = ctx.genders().orElse(GenderIdentity.defaultDatasets());
        EnumSet<Ethnicity> ethnicities = ctx.ethnicities().orElse(Ethnicity.defaultDatasets());
        EnumSet<Decade> decades = ctx.decades().orElse(Decade.defaultDatasets());
        return new FirstNameDatasetKey(KEY_ID, genders, ethnicities, decades);
    }

    @Override
    public String toString() {
        StringBuilder sb =  new StringBuilder(0).append(id);
        if(genders != null) sb.append("$").append(genders.stream().map(GenderIdentity::getPlaceholder).collect(Collectors.joining("|")));
        if(ethnicities != null) sb.append("$").append(ethnicities.stream().map(Ethnicity::getPlaceholder).collect(Collectors.joining("|")));
        if(decades != null) sb.append("$").append(decades.stream().map(Decade::getPlaceholder).collect(Collectors.joining("|")));
        return sb.toString();
    }
}
