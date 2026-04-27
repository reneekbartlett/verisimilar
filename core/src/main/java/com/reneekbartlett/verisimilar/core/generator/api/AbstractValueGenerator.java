package com.reneekbartlett.verisimilar.core.generator.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public abstract class AbstractValueGenerator<T> implements ValueGenerator<T> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractValueGenerator.class);

    /**
     * Core generation method subclasses must implement.
     * This is the only required override.
     */
    protected abstract T generateValue(DatasetResolutionContext context, SelectionFilter filter);

    @Override
    public T generate() {
        return generate(null, null); // SelectionFilter.empty()?
    }

    @Override
    public T generate(DatasetResolutionContext ctx) {
        return generate(ctx, null);
    }

    @Override
    public T generate(SelectionFilter filter) {
        return generate(null, filter);
    }

    /**
     * Entry point.
     */
    @Override
    public T generate(DatasetResolutionContext context, SelectionFilter filter) {
        if(context == null) context = DatasetResolutionContext.empty();
        if(filter == null) filter = SelectionFilter.empty();

        T record = generateValue(context, filter);

        return postProcess(record);
    }

    /**
     * Optional post-processing hook.
     */
    protected T postProcess(T result) {

        if(result == null) {
            //return "?????";
            //TODO:  Return default of type?
            //TODO:  Try another value?
            LOGGER.debug("empty/null value.");
            return generate();
        }

        return result;
    }

    /**
     * Subclasses must declare the record type.
     */
    protected abstract Class<T> valueType();
}
