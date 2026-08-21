package com.reneekbartlett.verisimilar.core.generator;

import java.util.List;

import com.reneekbartlett.verisimilar.core.datasets.key.DomainDatasetKey;
import com.reneekbartlett.verisimilar.core.model.DomainRecord;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

/***
 * 
 */
public class DomainRecordGenerator extends AbstractValueGenerator<DomainRecord> {
    private final DomainSelectionEngine domainSelector;
    @SuppressWarnings("unused")
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

        DomainType domainType = filter.domainType().orElse(generateDomainType(filter));
        //DomainDatasetKey key = DomainDatasetKey.fromDomainType(domainType);
        DomainDatasetKey key = DomainDatasetKey.fromContext(ctx);
        //LOGGER.debug("DomainDatasetKey={}", key);

        String domain;
        SelectionFilter domainFilter;
        if(filter.domainType().isEmpty()) {
            SelectionFilter.Builder domainFilterBldr = SelectionFilter.toBuilder(filter);
            domainFilter = domainFilterBldr.domainType(domainType).build();
        } else {
            domainFilter = filter;
        }

        domain = generateDomain(key, domainFilter);

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
