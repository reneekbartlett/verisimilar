package com.reneekbartlett.verisimilar.core.datasets.result;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.selector.engine.FirstNameSelectionEngine.NameKey;

public record MiddleNameDatasetResult(
        Map<NameKey, Map<String, Double>> datasets
) implements DatasetResult {
    public MiddleNameDatasetResult(Map<String, Double> femaleNames, Map<String, Double> maleNames) {
        this(
                Map.of(
                        new NameKey(GenderIdentity.FEMALE, null), femaleNames,
                        new NameKey(GenderIdentity.MALE, null), maleNames
                )
        );
    }

    public Map<String, Double> get(GenderIdentity gender) {
        return datasets.getOrDefault(new NameKey(gender, null), getDefault());
    }

    public Map<String, Double> get(GenderIdentity gender, Ethnicity ethnicity) {
        return datasets.getOrDefault(new NameKey(gender, ethnicity), get(gender));
    }

    @Override
    public Map<String, Double> getDefault() {
        return datasets.getOrDefault(new NameKey(GenderIdentity.FEMALE), HashMap.newHashMap(0));
    }

    public Map<String, Double> get(NameKey nameKey) {
        return datasets.getOrDefault(nameKey, getDefault());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0);
        sb.append("datasets:" + datasets.size());
        return sb.toString();
    }
}
