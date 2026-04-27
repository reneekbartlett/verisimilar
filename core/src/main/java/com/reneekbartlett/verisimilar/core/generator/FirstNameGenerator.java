package com.reneekbartlett.verisimilar.core.generator;

import com.reneekbartlett.verisimilar.core.datasets.key.FirstNameDatasetKey;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.FirstNameSelectionEngine;

public class FirstNameGenerator extends AbstractStringGenerator {

    private final FirstNameSelectionEngine selector;

    public FirstNameGenerator(FirstNameSelectionEngine selector) {
        this.selector = selector;
    }

    @Override
    protected String generateString(DatasetResolutionContext ctx, SelectionFilter criteria) {
        FirstNameDatasetKey key = new FirstNameDatasetKey(ctx.gender());
        return generateFirstName(key, criteria);
    }

    private String generateFirstName(FirstNameDatasetKey key, SelectionFilter filter) {
        return selector.select(key, filter);
    }

}
