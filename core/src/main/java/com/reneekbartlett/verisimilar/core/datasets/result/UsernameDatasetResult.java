package com.reneekbartlett.verisimilar.core.datasets.result;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine.NameKey;

public record UsernameDatasetResult(
        Map<String, Double> keywords,
        Map<NameKey, Map<String, Double>> datasets
) implements DatasetResult {
    public UsernameDatasetResult(Map<String, Double> keywords) {
        this(keywords, Map.of(new NameKey(), keywords));
    }

    @Override
    public Map<String, Double> getDefault() {
        return datasets.getOrDefault(new NameKey(), HashMap.newHashMap(0));
    }

    public Map<String, Double> get(NameKey nameKey) {
        return datasets.getOrDefault(nameKey, getDefault());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0);
        if(keywords != null) sb.append("keywords:" + keywords.size());
        if(datasets != null) sb.append(",datasets:" + datasets.size());
        return sb.toString();
    }
}
