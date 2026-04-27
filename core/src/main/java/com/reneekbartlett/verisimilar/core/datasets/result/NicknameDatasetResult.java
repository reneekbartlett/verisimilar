package com.reneekbartlett.verisimilar.core.datasets.result;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.selector.engine.NicknameSelectionEngine.NameKey;

public record NicknameDatasetResult(Map<NameKey, Map<String, Double>> datasets) implements DatasetResult {

    public NicknameDatasetResult(Map<String, Double> femaleNames, Map<String, Double> maleNames) {
        this(
                Map.of(
                        new NameKey(GenderIdentity.FEMALE, null), femaleNames, 
                        new NameKey(GenderIdentity.MALE, null), maleNames
                )
        );
    }

    public Map<String, Double> get(GenderIdentity gender) {
        return datasets.getOrDefault(new NameKey(gender, null), HashMap.newHashMap(0));
    }

    @Override
    public Map<String, Double> getDefault() {
        return datasets.get(new NameKey(GenderIdentity.FEMALE, null));
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
