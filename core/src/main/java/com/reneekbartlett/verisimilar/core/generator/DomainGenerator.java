package com.reneekbartlett.verisimilar.core.generator;

import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.DomainDatasetKey;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;

/***
 * 
 */
public class DomainGenerator extends AbstractStringGenerator {

    private final DomainSelectionEngine domainSelector;

    public DomainGenerator(DomainSelectionEngine domainSelector) {
        this.domainSelector = domainSelector;
    }

    public DomainGenerator(DatasetSelectionEngineRegistry selectors) {
        this(selectors.domain());
    }

    @Override
    protected String generateString(DatasetResolutionContext ctx, SelectionFilter filter) {
        DomainDatasetKey key = DomainDatasetKey.fromContext(ctx);
        return generateDomain(key, filter);
    }

    private String generateDomain(DomainDatasetKey key, SelectionFilter filter) {
        // TODO: Revisit this feature.
        if(filter.domainType().isEmpty()) {
            DomainType domainType = getRandomDomainType();
            filter = filter.toBuilder().domainType(domainType).build();
        }

        String randomDomain = domainSelector.select(key, filter);
        return randomDomain.toUpperCase();
    }

    private DomainType getRandomDomainType() {
        Map<DomainType, Double> domainTypesMap = DomainType.defaultMap();
        RandomSelector<DomainType> selector = new WeightedSelectorImpl<>(domainTypesMap);
        return selector.select();
    }
}
