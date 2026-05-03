package com.reneekbartlett.verisimilar.core.selector;

import java.util.Map;
import java.util.Set;

import com.reneekbartlett.verisimilar.core.model.TemplateField;

public interface SelectorStrategy<T> {
    RandomSelector<T> buildSelector(Map<T, Double> weights, TemplateField field);

    RandomSelector<T> buildSelector(Set<T> weights, TemplateField field);

    T select(Map<T, Double> map, TemplateField field);

    T select(T[] values, TemplateField field);

    String getType();
}
