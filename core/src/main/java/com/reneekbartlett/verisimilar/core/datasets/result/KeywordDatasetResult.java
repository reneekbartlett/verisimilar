package com.reneekbartlett.verisimilar.core.datasets.result;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.reneekbartlett.verisimilar.core.selector.engine.KeywordSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.KeywordSelectionEngine.NameKey;

public record KeywordDatasetResult(
        Map<String, Double> keywords,
        Set<String> templates,
        Map<KeywordSelectionEngine.NameKey, Map<String, Double>> datasets
) implements DatasetResult {
    public KeywordDatasetResult(Map<String, Double> keywords) {
        this(keywords, null, Map.of(new NameKey(), keywords));
    }

    public KeywordDatasetResult(Map<String, Double> keywords, Set<String> templates) {
        this(keywords, templates, Map.of(new NameKey(), keywords));
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
        if(templates != null) sb.append(",templates:" + templates.size());
        if(datasets != null) sb.append(",datasets:" + datasets.size());
        return sb.toString();
    }
}
