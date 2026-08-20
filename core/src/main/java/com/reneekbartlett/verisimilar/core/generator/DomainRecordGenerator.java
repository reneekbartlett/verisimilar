package com.reneekbartlett.verisimilar.core.generator;

import java.util.List;

import com.reneekbartlett.verisimilar.core.datasets.key.DomainDatasetKey;
import com.reneekbartlett.verisimilar.core.model.DomainRecord;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.UniformSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;

/***
 * 
 */
public class DomainRecordGenerator extends AbstractValueGenerator<DomainRecord> {
    private final DomainSelectionEngine domainSelector;
    private final List<TemplateField> filterFields;

    public DomainRecordGenerator(DomainSelectionEngine domainSelector) {
        this.domainSelector = domainSelector;
        this.filterFields = List.of(TemplateField.DOMAIN, TemplateField.DOMAIN_TYPE);
        // todo: domainTypeSelector?
    }

    public DomainRecordGenerator(DatasetSelectionEngineRegistry selectors) {
        this(selectors.domain());
    }

    @Override
    protected DomainRecord generateValue(DatasetResolutionContext ctx, SelectionFilter filter) {
        DomainDatasetKey key = DomainDatasetKey.fromContext(ctx);

        DomainType domainType = filter.domainType().orElseGet(() -> {
            return generateDomainType(filter);
        });

        String domain;
        if(filter.domainType().isEmpty()) {
            SelectionFilter domainFilter = SelectionFilter.toBuilder(filter).domainType(domainType).build();
            domain = generateDomain(key, domainFilter);
        } else {
            domain = generateDomain(key, filter);
        }

        return new DomainRecord(domain, domainType);
    }

    private String generateDomain(DomainDatasetKey key, SelectionFilter filter) {
        return domainSelector.select(key, filter);
    }

    private DomainType generateDomainType(SelectionFilter filter) {
        RandomSelector<DomainType> domainTypeSelector = new WeightedSelectorImpl<>(DomainType.defaultMap(), TemplateField.DOMAIN_TYPE);
        return domainTypeSelector.select();
    }

    @Override
    protected Class<DomainRecord> valueType() {
        return DomainRecord.class;
    }
}
