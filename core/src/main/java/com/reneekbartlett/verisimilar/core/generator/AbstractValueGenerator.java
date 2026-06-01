package com.reneekbartlett.verisimilar.core.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public abstract class AbstractValueGenerator<T> implements ValueGenerator<T> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractValueGenerator.class);

    /**
     * Main entry point for value generation.
     */
    protected abstract T generateValue(DatasetResolutionContext context, SelectionFilter filter);

    /**
     * Value type, i.e.
     */
    protected abstract Class<T> valueType();

    @Override
    public T generate(DatasetResolutionContext context, SelectionFilter filter) {
        if(context == null) context = DatasetResolutionContext.builder().build();
        if(filter == null) filter = SelectionFilter.builder().build();
        T record = generateValue(context, filter);
        return postProcess(record);
    }

    @Override
    public T generate() {
        return generate(null, null);
    }

    @Override
    public T generate(SelectionFilter filter) {
        return generate(null, filter);
    }

    /**
     * Optional post-processing hook.
     */
    protected T postProcess(T result) {
        if(result == null) {
            //TODO:  DECIDE. Return default of type? Try another value?
            LOGGER.warn("postProcess - empty/null value.");
            return generate();
        }
        return result;
    }
}
