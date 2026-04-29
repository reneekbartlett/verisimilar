package com.reneekbartlett.verisimilar.core.datasets.result;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine.NameKey;

public record DomainDatasetResult(
        Map<NameKey, Map<String, Double>> datasets
) implements DatasetResult {
    public DomainDatasetResult(
            Map<String, Double> b2c, 
            Map<String, Double> b2b, 
            Map<String, Double> edu, 
            Map<String, Double> gov, 
            Map<String, Double> disposable
    ) {
        this(
                Map.of(
                        new NameKey(DomainType.B2C), b2c, 
                        //new NameKey(DomainType.B2B), b2b, 
                        new NameKey(DomainType.EDU), edu,
                        new NameKey(DomainType.GOV), gov
                        //new NameKey(DomainType.DISPOSABLE), disposable
                )
        );
    }

    public Map<String, Double> get(DomainType domainType) {
        return datasets.getOrDefault(new NameKey(domainType), HashMap.newHashMap(0));
    }

    @Override
    public Map<String, Double> getDefault() {
        return get(DomainType.B2C);
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
