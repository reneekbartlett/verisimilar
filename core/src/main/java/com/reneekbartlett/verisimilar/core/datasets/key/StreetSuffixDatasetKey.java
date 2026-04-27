package com.reneekbartlett.verisimilar.core.datasets.key;

public record StreetSuffixDatasetKey(String id) implements DatasetKey {

    public static final String KEY_ID = "STREETSUFFIX";

    public StreetSuffixDatasetKey() {
        this(KEY_ID);
    }

    public static StreetSuffixDatasetKey defaults() {
        return new StreetSuffixDatasetKey(KEY_ID);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0).append(id);
        return sb.toString();
    }
}
