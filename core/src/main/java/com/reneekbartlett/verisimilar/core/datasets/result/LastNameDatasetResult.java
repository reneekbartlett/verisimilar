package com.reneekbartlett.verisimilar.core.datasets.result;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.selector.engine.LastNameSelectionEngine.NameKey;

public record LastNameDatasetResult(Map<NameKey, Map<String, Double>> datasets) implements DatasetResult {

    public Map<String, Double> get(Ethnicity ethnicity) {
        return datasets.getOrDefault(new NameKey(ethnicity), getDefault());
    }

    public Map<String, Double> getDefault() {
        return datasets.getOrDefault(new NameKey(Ethnicity.UNKNOWN), HashMap.newHashMap(0));
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
