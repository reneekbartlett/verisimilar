package com.reneekbartlett.verisimilar.core.datasets.key;

import com.reneekbartlett.verisimilar.core.model.GenderIdentity;

public record NicknameDatasetKey(
        String id,
        GenderIdentity gender
) implements DatasetKey {

    public static final String KEY_ID = "NICKNAME";

    public static NicknameDatasetKey defaults() {
        return new NicknameDatasetKey(KEY_ID, null);
    }

    public NicknameDatasetKey(GenderIdentity gender) {
        this(KEY_ID, gender);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0).append(id);
        if(gender != null) sb.append("$").append(gender.getLabel());
        return sb.toString();
    }
}
