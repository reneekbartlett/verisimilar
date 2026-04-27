package com.reneekbartlett.verisimilar.core.generator;

import java.time.LocalDate;

import com.reneekbartlett.verisimilar.core.generator.api.AbstractValueGenerator;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public abstract class AbstractLocalDateGenerator extends AbstractValueGenerator<LocalDate> {

    @Override
    protected final Class<LocalDate> valueType() {
        return LocalDate.class;
    }

    /**
     * Subclasses implement this to generate a raw string value.
     */
    protected abstract LocalDate generateLocalDate(DatasetResolutionContext context, SelectionFilter filter);

    @Override
    protected final LocalDate generateValue(DatasetResolutionContext context, SelectionFilter filter) {
        return generateLocalDate(context, filter);
    }
}
