package com.reneekbartlett.verisimilar.core.selector.filter;

import java.util.Objects;

@FunctionalInterface
public interface SelectionPredicate<T> {
    boolean test(T value);

    public default String asString() {
        return "[SelectionPredicate]";
    }

    /**
     * Combines this predicate with another using logical AND (&&).
     */
    default SelectionPredicate<T> and(SelectionPredicate<? super T> other) {
        Objects.requireNonNull(other);
        return (value) -> this.test(value) && other.test(value);
    }

    /**
     * Combines this predicate with another using logical OR (||).
     */
    default SelectionPredicate<T> or(SelectionPredicate<? super T> other) {
        Objects.requireNonNull(other);
        return (value) -> this.test(value) || other.test(value);
    }

    /**
     * Negates the current predicate (!).
     */
    default SelectionPredicate<T> negate() {
        return (value) -> !this.test(value);
    }

    static <T> SelectionPredicate<T> isEqual(Object targetRef) {
        return (null == targetRef)
                ? Objects::isNull
                : object -> targetRef.equals(object);
    }
}
