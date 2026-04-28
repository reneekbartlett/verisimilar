package com.reneekbartlett.verisimilar.core.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.generator.api.AbstractValueGenerator;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public abstract class AbstractStringGenerator extends AbstractValueGenerator<String> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractStringGenerator.class);

    @Override
    protected final Class<String> valueType() {
        return String.class;
    }

    /**
     * Subclasses implement this to generate a raw string value.
     */
    protected abstract String generateString(DatasetResolutionContext context, SelectionFilter criteria);

    @Override
    protected final String generateValue(DatasetResolutionContext context, SelectionFilter criteria) {
        return generateString(context, criteria);
    }

    @Override
    protected String postProcess(String value) {
        // Default normalization: trim + collapse whitespace
        if (value == null) return null;
        return value.trim().replaceAll("\\s+", " ");
    }
}
