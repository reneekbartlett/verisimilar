package com.reneekbartlett.verisimilar.core.datasets.result;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine.NameKey;

public record UsernameDatasetResult(
        Map<String, Double> keywords,
        Set<String> templates,
        Map<UsernameSelectionEngine.NameKey, Map<String, Double>> datasets
) implements DatasetResult {
    public UsernameDatasetResult(Map<String, Double> keywords) {
        this(keywords, null, Map.of(new NameKey(), keywords));
    }

    public UsernameDatasetResult(Map<String, Double> keywords, Set<String> templates) {
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
