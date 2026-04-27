package com.reneekbartlett.verisimilar.core.selector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class WeightedSelectorStrategy<T> implements SelectorStrategy<T> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(WeightedSelectorStrategy.class);

    @Override
    public RandomSelector<T> buildSelector(Map<T, Double> weights){
        return new WeightedSelectorImpl<>(weights);
    }

    @Override
    public RandomSelector<T> buildSelector(Set<T> values) {
        Map<T, Double> weights = HashMap.newHashMap(values.size());
        final double w = 1.0000/values.size();
        values.forEach(x -> weights.put(x, w));
        return new WeightedSelectorImpl<>(weights);
    }

    @Override
    public T select(Map<T, Double> map) {
        RandomSelector<T> selector = new WeightedSelectorImpl<>(map);
        return selector.select();
    }

    @Override
    public T selectWithFilter(Map<T, Double> map, SelectionFilter filter) {
        RandomSelector<T> selector = new WeightedSelectorImpl<>(map);
        selector.setFilter(filter);
        return selector.select();
    }

    @Override
    public T select(T[] values) {
        Map<T, Double> valueMap = HashMap.newHashMap(values.length);
        Double w = 1.000;
        Double decreaseAmt = 0.001;
        for(T v : values) {
            valueMap.put(v, w);
            w = w-decreaseAmt;
        }
        RandomSelector<T> selector = new WeightedSelectorImpl<>(valueMap);
        return selector.select();
    }

    @Override
    public T select(List<T> values) {
        Map<T, Double> valueMap = HashMap.newHashMap(values.size());
        Double w = 1.000;
        Double decreaseAmt = 0.001;
        for(T v : values) {
            valueMap.put(v, w);
            w = w-decreaseAmt;
        }
        RandomSelector<T> selector = new WeightedSelectorImpl<>(valueMap);
        return selector.select();
    }

    @Override
    public String getType() {
        return "WEIGHTED";
    }
}
