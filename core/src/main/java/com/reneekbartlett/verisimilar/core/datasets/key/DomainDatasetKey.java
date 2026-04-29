package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.Set;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;

public record DomainDatasetKey(String id, Set<DomainType> domainTypes, Integer year) implements DatasetKey {

    public static final String KEY_ID = "DOMAINS";

    public DomainDatasetKey() {
        this(KEY_ID, defaultDomainTypes(), null);
    }

    public DomainDatasetKey(Set<DomainType> domainTypes) {
        this(KEY_ID, domainTypes, null);
    }

    public DomainDatasetKey(Integer year) {
        this(KEY_ID, defaultDomainTypes(), year);
    }

    public static DomainDatasetKey defaults() {
        return new DomainDatasetKey(KEY_ID, defaultDomainTypes(), null);
    }

    public static Set<DomainType> defaultDomainTypes() {
        // TODO:  Add in DomainType.B2B, DomainType.DISPOSABLE
        return Set.of(DomainType.B2C, DomainType.EDU, DomainType.GOV);
    }

    public static DomainDatasetKey fromContext(DatasetResolutionContext ctx) {
        Set<DomainType> domainTypes = ctx.domainTypes() != null ? ctx.domainTypes() : defaultDomainTypes();
        return new DomainDatasetKey(domainTypes);
    }

    @Override
    public String toString() {
        StringBuilder sb =  new StringBuilder(0).append(id);
        if(domainTypes != null) sb.append("$").append(domainTypes.stream().map(Enum::name).collect(Collectors.joining(",")));
        return sb.toString();
    }
}
