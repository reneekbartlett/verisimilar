package com.reneekbartlett.verisimilar.core.selector;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public interface SelectorStrategy<T> {
    RandomSelector<T> buildSelector(Map<T, Double> weights);

    RandomSelector<T> buildSelector(Set<T> weights);

    T select(Map<T, Double> map);

    T select(T[] values);

    T select(List<T> valueList);

    T selectWithFilter(Map<T, Double> map, SelectionFilter filter);

    String getType();
}
