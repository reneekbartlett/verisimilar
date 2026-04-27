package com.reneekbartlett.verisimilar.core.datasets.result;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.selector.engine.StreetNameSelectionEngine.NameKey;

public record StreetNameDatasetResult(
        String[] all,
        Map<NameKey, Map<String, Double>> datasets
) implements DatasetResult {

    public StreetNameDatasetResult(String[] all) {
        this(all, Map.of(new NameKey(), toWeightedMap(all)));
    }

    public StreetNameDatasetResult(Map<NameKey, Map<String, Double>> datasets) {
        this(new String[0], datasets);
    }

    @Override
    public Map<String, Double> getDefault() {
        return datasets.values().stream().findFirst().orElse(HashMap.newHashMap(0));
    }

    public Map<String, Double> get(NameKey nameKey) {
        return datasets.getOrDefault(nameKey, getDefault());
    }

    public static Map<String, Double> toWeightedMap(String[] all){
        Map<String, Double> weightedMap = new HashMap<>();
        for(String val : all) {
            weightedMap.put(val, 0.0001);
        }
        return weightedMap;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0);
        sb.append("StreetNames:" + all.length);
        sb.append(",datasets:" + datasets.size());
        return sb.toString();
    }
}
