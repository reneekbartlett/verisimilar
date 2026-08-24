package com.reneekbartlett.verisimilar.core.generator;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.WeightedEnumData;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public abstract class AbstractEnumGenerator<E extends Enum<E> & WeightedEnumData> extends AbstractValueGenerator<E> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractEnumGenerator.class);

    // A List provides fast, O(1) random index access which EnumSet lacks
    protected final List<E> allowedValues;

    protected final Class<E> enumClass;

    protected AbstractEnumGenerator(Class<E> enumClass) {
        this.enumClass = enumClass;
        this.allowedValues = new ArrayList<>(EnumSet.allOf(enumClass));
    }

    protected AbstractEnumGenerator(EnumSet<E> enumSet) {
        if (enumSet == null || enumSet.isEmpty()) {
            throw new IllegalArgumentException("EnumSet cannot be null or empty");
        }

        // Grab the first element, then get its root Enum class type
        this.enumClass = enumSet.iterator().next().getDeclaringClass();

        // Copy to a List for efficient index-based retrieval
        this.allowedValues = new ArrayList<>(enumSet);
    }

    // Shared helper method available to all subclasses
    protected E getRandomValue() {
        int randomIndex = ThreadLocalRandom.current().nextInt(allowedValues.size());
        return allowedValues.get(randomIndex);
    }

    //@Override
    //protected final Class<E> valueType() {
    //    return this.enumClass;
    //}

    //protected E getRandomEnumConstant() {
    //    E[] constants = enumClass.getEnumConstants();
    //    int randomIndex = // logic to get random index
    //    return constants[randomIndex];
    //}

    /**
     * Subclasses implement this to generate a raw string value.
     */
    protected abstract E generateEnum(DatasetResolutionContext context, SelectionFilter filter);

    @Override
    protected final E generateValue(DatasetResolutionContext context, SelectionFilter filter) {
        return generateEnum(context, filter);
    }

    @Override
    protected E postProcess(E value) {
        // Default normalization: trim + collapse whitespace
        if (value == null) return null;
        return value;
    }
}
