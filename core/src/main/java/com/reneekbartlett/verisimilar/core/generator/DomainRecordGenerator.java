package com.reneekbartlett.verisimilar.core.generator;

import com.reneekbartlett.verisimilar.core.datasets.key.DomainDatasetKey;
import com.reneekbartlett.verisimilar.core.model.DomainRecord;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;

/***
 * 
 */
public class DomainRecordGenerator extends AbstractValueGenerator<DomainRecord> {
    private final DomainSelectionEngine domainSelector;

    public DomainRecordGenerator(DomainSelectionEngine domainSelector) {
        this.domainSelector = domainSelector;
    }

    public DomainRecordGenerator(DatasetSelectionEngineRegistry selectors) {
        this(selectors.domain());
    }

    @Override
    protected DomainRecord generateValue(DatasetResolutionContext ctx, SelectionFilter filter) {
        DomainDatasetKey key = DomainDatasetKey.fromContext(ctx);
        return generateDomain(key, filter);
    }

    private DomainRecord generateDomain(DomainDatasetKey key, SelectionFilter filter) {
        DomainType domainType = generateDomainType(filter);

        String valueFilter = filter.equalToMap().get(TemplateField.DOMAIN);
        if (valueFilter != null) {
            return new DomainRecord(valueFilter, domainType);
        }
        String domain = domainSelector.select(key, filter);
        return new DomainRecord(domain, null);
    }

    private DomainType generateDomainType(SelectionFilter filter) {
        String valueFilter = filter.equalToMap().get(TemplateField.DOMAIN_TYPE);
        if (valueFilter != null) {
            return DomainType.fromText(valueFilter);
        }
        RandomSelector<DomainType> domainTypeSelector = new WeightedSelectorImpl<>(DomainType.defaultMap(), TemplateField.DOMAIN_TYPE);
        return domainTypeSelector.select();
    }

    @Override
    protected Class<DomainRecord> valueType() {
        return DomainRecord.class;
    }
}
