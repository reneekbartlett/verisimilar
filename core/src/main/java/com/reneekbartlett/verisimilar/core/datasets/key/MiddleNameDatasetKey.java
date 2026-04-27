package com.reneekbartlett.verisimilar.core.datasets.key;

import com.reneekbartlett.verisimilar.core.model.GenderIdentity;

/***
 * GenderIdentity gender
 * Integer year
 * TODO: Generation
 * TODO: Regions/States/Location
 */
public record MiddleNameDatasetKey(
        String id,
        GenderIdentity gender,
        Integer year
) implements DatasetKey {

    public static final String KEY_ID = "MIDDLENAME";

    public static MiddleNameDatasetKey defaults() {
        return new MiddleNameDatasetKey(KEY_ID, null, null);
    }

    public MiddleNameDatasetKey(GenderIdentity gender) {
        this(KEY_ID, gender, null);
    }

    public MiddleNameDatasetKey(GenderIdentity gender, Integer year) {
        this(KEY_ID, gender, year);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0).append(id);
        if(gender != null) sb.append("$").append(gender.getLabel());
        if(year != null) sb.append("$").append(year);
        return sb.toString();
    }
}
