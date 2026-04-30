package com.reneekbartlett.verisimilar.core.generator;

import com.reneekbartlett.verisimilar.core.datasets.key.LastNameDatasetKey;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.LastNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;

public class LastNameGenerator extends AbstractStringGenerator {
    private final LastNameSelectionEngine selector;

    public LastNameGenerator(LastNameSelectionEngine selector) {
        this.selector = selector;
    }

    public LastNameGenerator(DatasetSelectionEngineRegistry selectors) {
        this(selectors.last());
    }

    @Override
    protected String generateString(DatasetResolutionContext ctx, SelectionFilter filter) {
        //LastNameDatasetKey key = new LastNameDatasetKey(ctx.ethnicities().orElse(Set.of(Ethnicity.UNKNOWN)));
        LastNameDatasetKey key = LastNameDatasetKey.fromContext(ctx);
        return generateLastName(key, filter);
    }

    private String generateLastName(LastNameDatasetKey key, SelectionFilter filter) {
        return this.selector.select(key, filter);
    }
}
