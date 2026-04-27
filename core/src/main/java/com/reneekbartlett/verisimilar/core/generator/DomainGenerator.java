package com.reneekbartlett.verisimilar.core.generator;

import com.reneekbartlett.verisimilar.core.datasets.key.DomainDatasetKey;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;

/***
 * 
 */
public class DomainGenerator extends AbstractStringGenerator {

    private final DomainSelectionEngine domainSelector;

    public DomainGenerator(DomainSelectionEngine domainSelector) {
        this.domainSelector = domainSelector;
    }

    @Override
    protected String generateString(DatasetResolutionContext ctx, SelectionFilter criteria) {
        return generateDomain(ctx, criteria);
    }

    private String generateDomain(DatasetResolutionContext ctx, SelectionFilter criteria) {

        DomainDatasetKey key = DomainDatasetKey.fromContext(ctx);

        String randomDomain = domainSelector.select(key, criteria);

        return randomDomain.toUpperCase();
    }
}
