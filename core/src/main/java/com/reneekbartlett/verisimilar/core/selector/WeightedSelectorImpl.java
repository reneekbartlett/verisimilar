package com.reneekbartlett.verisimilar.core.selector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.EntryFilter;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public final class WeightedSelectorImpl<T> implements RandomSelector<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeightedSelectorImpl.class);

    private final Map<T, Double> dataset;
    private final TemplateField field;
    private final List<T> items;
    private final double[] cumulative;
    private final int valueCount;

    private volatile SelectionFilter filter;

    // memoization cache
    private final ConcurrentMap<SelectionFilter, WeightedSelectorImpl<T>> filteredCache = new ConcurrentHashMap<>();

    private record Weights<T>(List<T> items, double[] cumulative) {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(0);
            if(items != null) sb.append("items.size:" + items.size());
            if(cumulative != null) sb.append(", cumulative.length:" + cumulative.length);
            return sb.toString();
        }
    }

    public WeightedSelectorImpl(Map<T, Double> dataset, TemplateField field) {
        Objects.requireNonNull(dataset, "dataset");
        if (dataset.isEmpty()) {
            LOGGER.error("Weighted dataset map cannot be empty");
            throw new IllegalArgumentException("Weighted dataset map cannot be empty");
        }
        this.field = field;
        this.dataset = Map.copyOf(dataset); // copy to ensure immutability

        Weights<T> weights = calcWeights(dataset);
        this.items = List.copyOf(weights.items());
        this.cumulative = weights.cumulative();
        this.valueCount = items.size();
    }

    @Override
    public T select() {
        double rand = ThreadLocalRandom.current().nextDouble();
        SelectionFilter filter = this.filter;
        if (filter != null && !filter.isEmpty()) {
            WeightedSelectorImpl<T> filtered = filteredCache.computeIfAbsent(filter, this::buildFilteredSelector);
            return filtered.select();
        }
        return selectUnfiltered(rand);
    }

    @Override
    public int getValueCount(){
        return this.valueCount;
    }

    private T selectUnfiltered(double rand) {
        int idx = Arrays.binarySearch(cumulative, rand);
        if (idx < 0) idx = -idx - 1;
        return items.get(idx);
    }

    public WeightedSelectorImpl<T> withFilter(SelectionFilter filter) {
        if (filter == null || filter.isEmpty()) {
            return this;
        }
        return filteredCache.computeIfAbsent(filter, this::buildFilteredSelector);
    }

    private WeightedSelectorImpl<T> buildFilteredSelector(SelectionFilter filter) {
        Map<T, Double> filtered = EntryFilter.applyToMap(dataset, filter, field);
        if (filtered.isEmpty()) {
            LOGGER.warn("Filtered dataset map empty for filter {}", filter);
            return new WeightedSelectorImpl<>(dataset, field); // fallback to original
        }
        LOGGER.debug("Filtered selector: original={}, filtered={}, filter={}",
                dataset.size(), filtered.size(), filter);
        return new WeightedSelectorImpl<>(filtered, field);
    }

    private Weights<T> calcWeights(Map<T, Double> weights) {
        int size = weights.size();
        List<T> vals = new ArrayList<>(size);
        double[] cumulative = new double[size];
        double running = 0.0;
        int i = 0;
        for (var entry : weights.entrySet()) {
            double w = entry.getValue();
            if (w <= 0.0) w = 0.0001;
            vals.add(entry.getKey());
            running += w;
            cumulative[i++] = running;
        }
        double inv = 1.0 / running;
        for (int j = 0; j < size; j++) {
            cumulative[j] *= inv;
        }
        return new Weights<>(vals,cumulative);
    }

    public void setFilter(SelectionFilter filter) {
        this.filter = filter;
    }
}
