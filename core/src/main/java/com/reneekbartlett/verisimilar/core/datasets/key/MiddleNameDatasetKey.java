package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.Set;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.model.GenderIdentity;

/***
 * GenderIdentity gender
 * Integer year
 * TODO: Generation
 * TODO: Regions/States/Location
 */
public record MiddleNameDatasetKey(
        String id,
        Set<GenderIdentity> genders,
        Integer year
) implements DatasetKey {

    public static final String KEY_ID = "MIDDLENAME";

    public static MiddleNameDatasetKey defaults() {
        return new MiddleNameDatasetKey(KEY_ID, null, null);
    }

    public MiddleNameDatasetKey(Set<GenderIdentity> genders) {
        this(KEY_ID, genders, null);
    }

    public MiddleNameDatasetKey(Set<GenderIdentity> genders, Integer year) {
        this(KEY_ID, genders, year);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0).append(id);
        //if(gender != null) sb.append("$").append(gender.getLabel());
        if(genders != null) sb.append("$").append(genders.stream().map(Enum::name).collect(Collectors.joining("|")));
        if(year != null) sb.append("$").append(year);
        return sb.toString();
    }
}
