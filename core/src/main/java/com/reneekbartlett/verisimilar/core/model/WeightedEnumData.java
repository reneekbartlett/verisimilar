package com.reneekbartlett.verisimilar.core.model;

import java.util.EnumSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 * 
 */
public interface WeightedEnumData {
    String getLabel();
    double getWeight();
    
    /**
     * Determines if this constant should be included in the default generation pool.
     * Overriding this allows individual enums to filter themselves out.
     */
    default boolean isIncludedByDefault() {
        return true; // Default behavior: include everything
    }

    /**
     * Generates a filtered EnumSet based on the isIncludedByDefault criteria.
     */
    static <E extends Enum<E> & WeightedEnumData> EnumSet<E> defaultSet(Class<E> enumClass) {

        //return EnumSet.allOf(enumClass);

        // Grab all constants, filter them by the interface rule, and collect into an EnumSet
        return Stream.of(enumClass.getEnumConstants())
                .filter(WeightedEnumData::isIncludedByDefault)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(enumClass)));
    }
}
