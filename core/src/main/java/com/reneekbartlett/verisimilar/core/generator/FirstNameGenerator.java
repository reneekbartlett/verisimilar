package com.reneekbartlett.verisimilar.core.generator;

import com.reneekbartlett.verisimilar.core.datasets.key.FirstNameDatasetKey;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.FirstNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;

public class FirstNameGenerator extends AbstractStringGenerator {

    private final FirstNameSelectionEngine selector;

    public FirstNameGenerator(FirstNameSelectionEngine selector) {
        this.selector = selector;
    }

    public FirstNameGenerator(DatasetSelectionEngineRegistry selectors) {
        this(selectors.first());
    }

    @Override
    protected String generateString(DatasetResolutionContext ctx, SelectionFilter filter) {
        // TODO:  Evaluate use of DatasetResolutionContext and DatasetKey's.
        FirstNameDatasetKey key = FirstNameDatasetKey.fromContext(ctx);
        return generateFirstName(key, filter);
    }

    private String generateFirstName(FirstNameDatasetKey key, SelectionFilter filter) {
        return selector.select(key, filter);
    }

}
