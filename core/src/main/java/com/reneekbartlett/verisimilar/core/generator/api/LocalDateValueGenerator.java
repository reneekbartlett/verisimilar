package com.reneekbartlett.verisimilar.core.generator.api;

import java.time.LocalDate;

import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public interface LocalDateValueGenerator {

    LocalDate generate();

    LocalDate generate(DatasetResolutionContext ctx);

    LocalDate generate(SelectionFilter criteria);

    LocalDate generate(DatasetResolutionContext context, SelectionFilter criteria);
}
