package com.reneekbartlett.verisimilar.core.datasets.key;

import com.reneekbartlett.verisimilar.core.model.Ethnicity;

/***
 * Ethnicity
 */
public record LastNameDatasetKey(String id, Ethnicity ethnicity) implements DatasetKey {

    public static final String KEY_ID = "LASTNAME";

    public static LastNameDatasetKey defaults() {
        return new LastNameDatasetKey(KEY_ID, Ethnicity.UNKNOWN);
    }

    public LastNameDatasetKey() {
        this(KEY_ID, Ethnicity.UNKNOWN);
    }

    public LastNameDatasetKey(Ethnicity ethnicity) {
        this(KEY_ID, ethnicity);
    }

    //public boolean isDefault() {
    //    return this.toString() == defaults().toString();
    //}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0).append(id);
        if(ethnicity != null) sb.append("$").append(ethnicity.name());
        return sb.toString();
    }
}
