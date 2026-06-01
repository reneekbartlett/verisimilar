package com.reneekbartlett.verisimilar.core.generator;

import java.time.LocalDate;

import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public interface LocalDateValueGenerator {
    LocalDate generate();
    LocalDate generate(SelectionFilter filter);
    LocalDate generate(DatasetResolutionContext context, SelectionFilter filter);
}
