package com.reneekbartlett.verisimilar.core.generator;

import com.reneekbartlett.verisimilar.core.datasets.key.LastNameDatasetKey;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.LastNameSelectionEngine;

public class LastNameGenerator extends AbstractStringGenerator {
    private final LastNameSelectionEngine selector;

    public LastNameGenerator(LastNameSelectionEngine selector) {
        this.selector = selector;
    }

    @Override
    protected String generateString(DatasetResolutionContext ctx, SelectionFilter filter) {
        LastNameDatasetKey key = new LastNameDatasetKey(ctx.ethnicity()
                .orElse(filter.ethnicity().orElse(Ethnicity.UNKNOWN)));
        return generateLastName(key, filter);
    }

    private String generateLastName(LastNameDatasetKey key, SelectionFilter filter) {
        return this.selector.select(key, filter);
    }
}
