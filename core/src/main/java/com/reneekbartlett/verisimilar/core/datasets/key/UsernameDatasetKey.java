package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.EnumSet;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.UsernameType;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;

/***
 * EnumSet<UsernameType>, EnumSet<GenderIdentity> genders
 */
public record UsernameDatasetKey(
        String id,
        EnumSet<UsernameType> usernameTypes,
        EnumSet<GenderIdentity> genders
) implements DatasetKey {

    public static final String KEY_ID = "USERNAME";

    public static UsernameDatasetKey defaults() {
        return new UsernameDatasetKey(KEY_ID, UsernameType.defaultDatasets(), GenderIdentity.defaultDatasets());
    }

    public UsernameDatasetKey() {
        this(KEY_ID, UsernameType.defaultDatasets(), GenderIdentity.defaultDatasets());
    }

    public UsernameDatasetKey(EnumSet<UsernameType> usernameTypes) {
        this(KEY_ID, usernameTypes, GenderIdentity.defaultDatasets());
    }

    public static UsernameDatasetKey fromContext(DatasetResolutionContext ctx) {
        EnumSet<UsernameType> usernameTypes = ctx.usernameTypes().orElse(UsernameType.defaultDatasets());
        EnumSet<GenderIdentity> genders = ctx.genders().orElse(GenderIdentity.defaultDatasets());
        return new UsernameDatasetKey(KEY_ID, usernameTypes, genders);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0).append(id);
        if(usernameTypes != null) sb.append("$").append(usernameTypes.stream().map(UsernameType::getPlaceholder).collect(Collectors.joining("|")));
        if(genders != null) sb.append("$").append(genders.stream().map(GenderIdentity::getPlaceholder).collect(Collectors.joining("|")));
        return sb.toString();
    }
}
