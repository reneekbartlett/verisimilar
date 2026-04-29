package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.Set;

import com.reneekbartlett.verisimilar.core.model.GenderIdentity;

/***
 * String[] usernameTypes, GenderIdentity gender
 */
public record KeywordDatasetKey(
        String id,
        Set<String> keywordTypes,
        GenderIdentity gender
) implements DatasetKey {

    public static final String KEY_ID = "KEYWORD";

    public static KeywordDatasetKey defaults() {
        return new KeywordDatasetKey(KEY_ID, defaultKeywordTypes(), null);
    }

    public KeywordDatasetKey() {
        this(KEY_ID, defaultKeywordTypes(), null);
    }

    public KeywordDatasetKey(Set<String> usernameTypes) {
        this(KEY_ID, usernameTypes, null);
    }

    public static Set<String> defaultKeywordTypes() {
        return Set.of("ALL");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0).append(id);
        if(keywordTypes != null) sb.append("$").append(String.join("$", keywordTypes));
        if(gender != null) sb.append("$").append(gender.getLabel());
        return sb.toString();
    }
}
