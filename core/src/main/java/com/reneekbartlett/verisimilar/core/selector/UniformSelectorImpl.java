package com.reneekbartlett.verisimilar.core.selector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.selector.filter.EntryFilter;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public final class UniformSelectorImpl<T> implements RandomSelector<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UniformSelectorImpl.class);

    private final List<T> items;
    private final int valueCount;

    private SelectionFilter filter;

    public UniformSelectorImpl(List<T> items) {
        Objects.requireNonNull(items, "items");
        if (items.isEmpty()) {
            LOGGER.error("Items cannot be empty");
            throw new IllegalArgumentException("Items cannot be empty");
        }
        this.items = items;
        this.valueCount = items.size();
    }

    public UniformSelectorImpl(Set<T> items) {
        Objects.requireNonNull(items, "items");
        if (items.isEmpty()) {
            LOGGER.error("Items cannot be empty");
            throw new IllegalArgumentException("Items cannot be empty");
        }
        this.items = new ArrayList<>(items);
        this.valueCount = items.size();
    }

    public UniformSelectorImpl(Map<T, Double> weights) {
        Objects.requireNonNull(weights, "weights");
        if (weights.isEmpty()) {
            LOGGER.error("Weighted map cannot be empty");
            throw new IllegalArgumentException("Weighted map cannot be empty");
        }
        this.items = new ArrayList<>(weights.keySet());
        this.valueCount = items.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T select() {
        if(filter !=null && !filter.isEmpty()) {
            List<String> stringList = items.stream().map(String::valueOf).toList();
            List<String> filteredList = EntryFilter.apply(stringList, filter);
            LOGGER.debug("select (filtered) - original.size():{}, filteredList.size():{}", stringList.size(), filteredList.size());
            if (!filteredList.isEmpty()) {
                int index = ThreadLocalRandom.current().nextInt(filteredList.size());
                return (T) filteredList.stream().skip(index).findFirst().orElseThrow();
            }
        }
        int index = ThreadLocalRandom.current().nextInt(items.size());
        return items.stream().skip(index).findFirst().orElseThrow();
    }

    @Override
    public int getValueCount(){
        return this.valueCount;
    }

    public void setFilter(SelectionFilter filter) {
        this.filter = filter;
    }

//    //@Override
//    private static <T> RandomSelector<T> buildRandomSelector(Map<T, Double> weights){
//        return new UniformSelectorImpl<>(weights);
//    }
//
//    //@Override
//    private static <T> RandomSelector<T> buildRandomSelector(Set<T> values) {
//        Map<T, Double> weights = HashMap.newHashMap(values.size());
//        values.forEach(x -> weights.put(x, 0.0001));
//        return new UniformSelectorImpl<>(weights);
//    }
}
