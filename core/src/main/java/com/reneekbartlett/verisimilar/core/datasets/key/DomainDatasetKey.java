package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.model.Decade;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;

/***
 * 
 */
public record DomainDatasetKey(
        String id,
        EnumSet<DomainType> domainTypes,
        EnumSet<Decade> decades
) implements DatasetKey {

    public static final String KEY_ID = "DOMAINS";

    public DomainDatasetKey() {
        this(KEY_ID, defaultDomainTypes(), Decade.defaultDatasets());
    }

    public DomainDatasetKey(DomainType domainType) {
        this(KEY_ID, EnumSet.of(domainType), Decade.defaultDatasets());
    }

    public DomainDatasetKey(EnumSet<Decade> decades) {
        this(KEY_ID, defaultDomainTypes(), decades);
    }

    @Override
    public Set<TemplateField> fields(){
        return EnumSet.of(TemplateField.DOMAIN_TYPE, TemplateField.DECADE);
    }

    public static DomainDatasetKey defaults() {
        return new DomainDatasetKey(KEY_ID, defaultDomainTypes(), null);
    }

    public static EnumSet<DomainType> defaultDomainTypes() {
        return EnumSet.of(DomainType.B2C, DomainType.B2B, DomainType.EDU, DomainType.GOV, DomainType.DISPOSABLE);
    }

    // TODO:  Add to others?
    public static DomainDatasetKey fromContext(DatasetResolutionContext ctx) {
        EnumSet<DomainType> domainTypes = ctx.domainTypes().orElse(DomainType.defaultDatasets());
        EnumSet<Decade> decades = ctx.decades().orElse(Decade.defaultDatasets());
        return new DomainDatasetKey(KEY_ID, domainTypes, decades);
    }

//    public static DomainDatasetKey fromDomainType(DomainType domainType) {
//        EnumSet<DomainType> domainTypes = EnumSet.of(domainType);
//        // TODO:  decade currently set to 2025
//        EnumSet<Decade> decades = Decade.defaultDatasets();
//        return new DomainDatasetKey(KEY_ID, domainTypes, decades);
//    }

    @Override
    public String toString() {
        StringBuilder sb =  new StringBuilder(0).append(id);
        if(domainTypes != null) sb.append("$").append(domainTypes.stream().map(DomainType::getPlaceholder).collect(Collectors.joining("|")));
        if(decades != null) sb.append("$").append(decades.stream().map(Decade::getPlaceholder).collect(Collectors.joining("|")));
        return sb.toString();
    }
}
