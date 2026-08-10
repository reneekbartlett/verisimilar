package com.reneekbartlett.verisimilar.core.generator;

import java.util.List;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public interface ValueGenerator<T> {
    T generate();
    T generate(SelectionFilter filter);
    T generate(DatasetResolutionContext context, SelectionFilter filter);
    List<TemplateField> filterFields();
}
