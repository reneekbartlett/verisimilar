package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.Set;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.model.Decade;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;

public record DomainDatasetKey(
        String id,
        Set<DomainType> domainTypes,
        Set<Decade> decades
) implements DatasetKey {

    public static final String KEY_ID = "DOMAINS";

    public DomainDatasetKey() {
        this(KEY_ID, defaultDomainTypes(), Decade.defaultDatasets());
    }

    public DomainDatasetKey(DomainType domainType) {
        this(KEY_ID, Set.of(domainType), Decade.defaultDatasets());
    }

    public DomainDatasetKey(Set<Decade> decades) {
        this(KEY_ID, defaultDomainTypes(), decades);
    }

    public static DomainDatasetKey defaults() {
        return new DomainDatasetKey(KEY_ID, defaultDomainTypes(), null);
    }

    public static Set<DomainType> defaultDomainTypes() {
        // TODO:  Add in DomainType.B2B, DomainType.DISPOSABLE
        return Set.of(DomainType.B2C, DomainType.EDU, DomainType.GOV);
    }

    // TODO:  Add to others?
    public static DomainDatasetKey fromContext(DatasetResolutionContext ctx) {
        Set<DomainType> domainTypes = ctx.domainTypes().orElse(DomainType.defaultDatasets());
        Set<Decade> decades = ctx.decades().orElse(Decade.defaultDatasets());
        return new DomainDatasetKey(KEY_ID, domainTypes, decades);
    }

    @Override
    public String toString() {
        StringBuilder sb =  new StringBuilder(0).append(id);
        if(domainTypes != null) sb.append("$").append(domainTypes.stream().map(DomainType::getPlaceholder).collect(Collectors.joining("|")));
        if(decades != null) sb.append("$").append(decades.stream().map(Decade::getPlaceholder).collect(Collectors.joining("|")));
        return sb.toString();
    }
}
