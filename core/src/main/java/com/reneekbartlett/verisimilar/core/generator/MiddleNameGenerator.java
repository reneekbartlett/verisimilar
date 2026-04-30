package com.reneekbartlett.verisimilar.core.generator;

import com.reneekbartlett.verisimilar.core.datasets.key.MiddleNameDatasetKey;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.MiddleNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;

public class MiddleNameGenerator extends AbstractStringGenerator {
    private final MiddleNameSelectionEngine selector;

    public MiddleNameGenerator(MiddleNameSelectionEngine selector) {
        this.selector = selector;
    }

    public MiddleNameGenerator(DatasetSelectionEngineRegistry selectors) {
        this(selectors.middle());
    }

    @Override
    protected String generateString(DatasetResolutionContext ctx, SelectionFilter filter) {
        //MiddleNameDatasetKey key = new MiddleNameDatasetKey(ctx.genders().orElse(GenderIdentity.defaults())
        //        //, ctx.decades().orElse(null)
        //);
        MiddleNameDatasetKey key = MiddleNameDatasetKey.fromContext(ctx);
        return generateMiddleName(key, filter);
    }

    private String generateMiddleName(MiddleNameDatasetKey key, SelectionFilter filter) {
        return selector.select(key, filter);
    }
}
