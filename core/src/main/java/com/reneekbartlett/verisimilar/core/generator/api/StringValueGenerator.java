package com.reneekbartlett.verisimilar.core.generator.api;

import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public interface StringValueGenerator {
    String generate();
    String generate(SelectionFilter criteria);
    String generate(DatasetResolutionContext context);
    String generate(DatasetResolutionContext context, SelectionFilter criteria);
}
