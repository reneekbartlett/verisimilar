package com.reneekbartlett.verisimilar.core.datasets.result;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.selector.engine.StreetSuffixSelectionEngine.NameKey;

public record StreetSuffixDatasetResult(
        //Map<String, String> suffixAbbr,
        Map<NameKey, Map<String, Double>> datasets
) implements DatasetResult {

    //public StreetSuffixDatasetResult(Map<String, String> suffixAbbr) {
    //    this(suffixAbbr, Map.of(new NameKey(), toWeightedMap(suffixAbbr.keySet())));
    //}

    @Override
    public Map<String, Double> getDefault() {
        return datasets.values().stream().findFirst().orElse(HashMap.newHashMap(0));
    }

    public Map<String, Double> get(NameKey nameKey) {
        return datasets.getOrDefault(nameKey, getDefault());
    }

//    public static Map<String, Double> toWeightedMap(Set<String> all){
//        Map<String, Double> weightedMap = new HashMap<>();
//        for(String val : all) {
//            weightedMap.put(val, 0.0001);
//        }
//        return weightedMap;
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0);
        //sb.append("suffixAbbr:" + suffixAbbr.size());
        sb.append("datasets:" + datasets.size());
        return sb.toString();
    }
}
