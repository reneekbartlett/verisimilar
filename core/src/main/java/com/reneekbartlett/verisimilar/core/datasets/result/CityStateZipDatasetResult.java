package com.reneekbartlett.verisimilar.core.datasets.result;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.selector.engine.CityStateZipSelectionEngine.NameKey;

public record CityStateZipDatasetResult(Map<NameKey, Map<String, Double>> datasets) implements DatasetResult {

    @Override
    public Map<String, Double> getDefault() {
        return datasets.values().stream().findFirst().orElse(HashMap.newHashMap(0));
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
        sb.append("datasets:" + datasets.size());
        return sb.toString();
    }
}
