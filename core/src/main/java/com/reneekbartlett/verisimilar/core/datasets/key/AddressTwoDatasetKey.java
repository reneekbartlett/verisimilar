package com.reneekbartlett.verisimilar.core.datasets.key;

public record AddressTwoDatasetKey(String id) implements DatasetKey {

    public static final String KEY_ID = "ADDRESSTWO";

    public AddressTwoDatasetKey() {
        this(KEY_ID);
    }

    public static AddressTwoDatasetKey defaults() {
        return new AddressTwoDatasetKey(KEY_ID);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0).append(id);
        return sb.toString();
    }
}
