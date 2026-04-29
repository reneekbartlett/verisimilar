package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.Set;

import com.reneekbartlett.verisimilar.core.model.GenderIdentity;

/***
 * String[] usernameTypes, GenderIdentity gender
 */
public record UsernameDatasetKey(
        String id,
        Set<String> usernameTypes,
        GenderIdentity gender
) implements DatasetKey {

    public static final String KEY_ID = "USERNAME";

    public static UsernameDatasetKey defaults() {
        return new UsernameDatasetKey(KEY_ID, defaultUsernameTypes(), null);
    }

    public UsernameDatasetKey() {
        this(KEY_ID, defaultUsernameTypes(), null);
    }

    public UsernameDatasetKey(Set<String> usernameTypes) {
        this(KEY_ID, usernameTypes, null);
    }

    public static Set<String> defaultUsernameTypes() {
        return Set.of("ALL");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0).append(id);
        if(usernameTypes != null) sb.append("$").append(String.join("$", usernameTypes));
        if(gender != null) sb.append("$").append(gender.getLabel());
        return sb.toString();
    }
}
