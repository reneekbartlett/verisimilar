package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.EnumSet;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.model.KeywordType;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;

/***
 * String[] usernameTypes, GenderIdentity gender
 */
public record KeywordDatasetKey(
        String id,
        EnumSet<KeywordType> keywordTypes
) implements DatasetKey {

    public static final String KEY_ID = "KEYWORD";

    public static KeywordDatasetKey defaults() {
        return new KeywordDatasetKey(KEY_ID, KeywordType.defaultDatasets());
    }

    public KeywordDatasetKey() {
        this(KEY_ID, KeywordType.defaultDatasets());
    }

    public KeywordDatasetKey(EnumSet<KeywordType> keywordTypes) {
        this(KEY_ID, keywordTypes);
    }

    public static KeywordDatasetKey fromContext(DatasetResolutionContext ctx) {
        EnumSet<KeywordType> keywordTypes = ctx.keywordTypes().orElse(KeywordType.defaultDatasets());
        return new KeywordDatasetKey(KEY_ID, keywordTypes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0).append(id);
        if(keywordTypes != null) sb.append("$").append(keywordTypes.stream().map(KeywordType::getPlaceholder).collect(Collectors.joining("|")));
        return sb.toString();
    }
}
