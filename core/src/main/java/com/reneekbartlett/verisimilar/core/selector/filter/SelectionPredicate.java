package com.reneekbartlett.verisimilar.core.selector.filter;

@FunctionalInterface
public interface SelectionPredicate<T> {
    boolean test(T value);

    public default String asString() {
        return "";
    }
}
