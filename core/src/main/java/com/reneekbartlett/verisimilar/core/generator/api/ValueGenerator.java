package com.reneekbartlett.verisimilar.core.generator.api;

import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public interface ValueGenerator<T> {
    T generate();
    T generate(DatasetResolutionContext ctx);
    T generate(SelectionFilter criteria);
    T generate(DatasetResolutionContext context, SelectionFilter criteria);
}
