package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.EnumSet;
import java.util.Set;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;

public record StreetSuffixDatasetKey(String id) implements DatasetKey {

    public static final String KEY_ID = "STREET_SUFFIX";

    public StreetSuffixDatasetKey() {
        this(KEY_ID);
    }

    @Override
    public Set<TemplateField> fields(){
        return EnumSet.of(TemplateField.ADDRESS_CATEGORY);
    }

    public static StreetSuffixDatasetKey defaults() {
        return new StreetSuffixDatasetKey(KEY_ID);
    }

    public static StreetSuffixDatasetKey fromContext(DatasetResolutionContext ctx) {
        return new StreetSuffixDatasetKey(KEY_ID);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0).append(id);
        return sb.toString();
    }
}
