package com.reneekbartlett.verisimilar.core.datasets.result;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.selector.engine.AddressTwoSelectionEngine.NameKey;

public record AddressTwoDatasetResult(
        Map<String, Double> all, 
        Map<NameKey, Map<String, Double>> datasets
) implements DatasetResult {

    public AddressTwoDatasetResult(Map<String, Double> all) {
        this(all, Map.of(new NameKey(), all));
    }

    @Override
    public Map<String, Double> getDefault() {
        return datasets.getOrDefault(new NameKey(), HashMap.newHashMap(0));
    }

    public Map<String, Double> get() {
        return datasets.getOrDefault(new NameKey(), getDefault());
    }

    public Map<String, Double> get(NameKey nameKey) {
        return datasets.getOrDefault(nameKey, getDefault());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0);
        sb.append("all:" + all.size());
        sb.append(", datasets:" + datasets.size());
        return sb.toString();
    }
}
