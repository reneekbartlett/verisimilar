package com.reneekbartlett.verisimilar.core.selector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.TemplateField;

public class WeightedSelectorStrategy<T> implements SelectorStrategy<T> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(WeightedSelectorStrategy.class);

    @Override
    public RandomSelector<T> buildSelector(Map<T, Double> weights, TemplateField field){
        return new WeightedSelectorImpl<>(weights, field);
    }

    @Override
    public RandomSelector<T> buildSelector(Set<T> values, TemplateField field) {
        Map<T, Double> weights = HashMap.newHashMap(values.size());
        final double w = 1.0000/values.size();
        values.forEach(x -> weights.put(x, w));
        return new WeightedSelectorImpl<>(weights, field);
    }

    @Override
    public T select(Map<T, Double> map, TemplateField field) {
        RandomSelector<T> selector = new WeightedSelectorImpl<>(map, field);
        return selector.select();
    }

    @Override
    public T select(T[] values, TemplateField field) {
        Map<T, Double> valueMap = HashMap.newHashMap(values.length);
        Double w = 1.000;
        Double decreaseAmt = 0.001;
        for(T v : values) {
            valueMap.put(v, w);
            w = w-decreaseAmt;
        }
        RandomSelector<T> selector = new WeightedSelectorImpl<>(valueMap, field);
        return selector.select();
    }

    @Override
    public String getType() {
        return "WEIGHTED";
    }
}
