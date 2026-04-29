package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.Set;

import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;

public record DomainDatasetKey(String id, Set<String> domainTypes, Integer year) implements DatasetKey {

    public static final String KEY_ID = "DOMAINS";

    public DomainDatasetKey() {
        this(KEY_ID, defaultDomainTypes(), null);
    }

    public DomainDatasetKey(Set<String> domainTypes) {
        this(KEY_ID, domainTypes, null);
    }

    public DomainDatasetKey(Integer year) {
        this(KEY_ID, defaultDomainTypes(), year);
    }

    public static DomainDatasetKey defaults() {
        return new DomainDatasetKey(KEY_ID, defaultDomainTypes(), null);
    }

    public static Set<String> defaultDomainTypes() {
        return Set.of("b2c", "b2b", "disposable", "edu", "gov");
    }

    public static DomainDatasetKey fromContext(DatasetResolutionContext ctx) {
        Set<String> domainTypes = ctx.domainTypes() != null ? Set.of(ctx.domainTypes()) : defaultDomainTypes();
        return new DomainDatasetKey(domainTypes);
    }

    @Override
    public String toString() {
        StringBuilder sb =  new StringBuilder(0).append(id);
        if(domainTypes != null) sb.append("$").append(String.join("$", domainTypes));
        return sb.toString();
    }
}
