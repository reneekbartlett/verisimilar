package com.reneekbartlett.verisimilar.core.selector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class UniformSelectorStrategy<T> implements SelectorStrategy<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UniformSelectorStrategy.class);

    private final Random random = new Random();

    @Override
    public RandomSelector<T> buildSelector(Map<T, Double> weights){
        return new UniformSelectorImpl<>(weights);
    }

    @Override
    public RandomSelector<T> buildSelector(Set<T> values) {
        Map<T, Double> weights = HashMap.newHashMap(values.size());
        values.forEach(x -> weights.put(x, 0.0001));
        return new UniformSelectorImpl<>(weights);
    }

    @Override
    public T select(Map<T, Double> map) {
        if (map.isEmpty()) {
            throw new IllegalArgumentException("Map cannot be empty");
        }

        RandomSelector<T> selector = new UniformSelectorImpl<>(map);
        var selected = selector.select();
        //LOGGER.debug("select - selected={}", selected);
        return selected;

        //TODO: return selector.select();
    }

    public T selectWithFilter(Map<T, Double> map, SelectionFilter filter) {
        if (map.isEmpty()) {
            throw new IllegalArgumentException("Map cannot be empty");
        }

        RandomSelector<T> selector = new UniformSelectorImpl<>(map);
        selector.setFilter(filter);

        var selected = selector.select();
        LOGGER.debug("selectWithFilter - selected={}", selected);
        return selected;

        //TODO: return selector.select();
    }

    @Override
    public T select(T[] values) {
        if(values.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty");
        }

        int index = random.nextInt(values.length);
        var selected = values[index];
        LOGGER.debug("selected={}", selected);
        return selected;

        //return values[index];
    }

    @Override
    public T select(List<T> valueList) {
        if(valueList.size() == 0) {
            throw new IllegalArgumentException("Array cannot be empty");
        }

        int index = random.nextInt(valueList.size());
        return valueList.get(index);
    }

    @Override
    public String getType() {
        return "UNIFORM";
    }
}
