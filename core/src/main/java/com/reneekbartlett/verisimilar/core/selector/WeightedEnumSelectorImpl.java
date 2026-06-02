package com.reneekbartlett.verisimilar.core.selector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.WeightedEnumData;
import com.reneekbartlett.verisimilar.core.selector.filter.EntryFilter;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public final class WeightedEnumSelectorImpl<E extends Enum<E> & WeightedEnumData> implements RandomSelector<E> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeightedSelectorImpl.class);

    private final EnumSet<E> enumSet;
    private final Map<E, Double> dataset;
    private final TemplateField field;
    private final List<E> items;
    private final double[] cumulative;
    private final int valueCount;

    private volatile SelectionFilter filter;

    // memoization cache
    private final ConcurrentMap<SelectionFilter, WeightedEnumSelectorImpl<E>> filteredCache = new ConcurrentHashMap<>();

    private record Weights<E>(List<E> items, double[] cumulative) {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(0);
            if(items != null) sb.append("items.size:" + items.size());
            if(cumulative != null) sb.append(", cumulative.length:" + cumulative.length);
            return sb.toString();
        }
    }

    public WeightedEnumSelectorImpl(EnumSet<E> enumSet, TemplateField field) {
        Objects.requireNonNull(enumSet, "enumSet");
        if (enumSet.isEmpty()) {
            LOGGER.error("Dataset cannot be empty");
            throw new IllegalArgumentException("Dataset cannot be empty");
        }
        this.field = field;
        this.enumSet = enumSet;

        Map<E, Double> dataset = HashMap.newHashMap(enumSet.size());
        for (E entry : enumSet) {
            dataset.put(entry, entry.getWeight());
        }
        this.dataset = Map.copyOf(dataset); // copy to ensure immutability

        Weights<E> weights = calcWeights(enumSet);
        this.items = List.copyOf(weights.items());
        this.cumulative = weights.cumulative();
        this.valueCount = this.items.size();
    }

    @Override
    public E select() {
        double rand = ThreadLocalRandom.current().nextDouble();
        SelectionFilter filter = this.filter;
        if (filter != null && !filter.isEmpty()) {
            WeightedEnumSelectorImpl<E> filtered = filteredCache.computeIfAbsent(filter, this::buildFilteredSelector);
            return filtered.select();
        }
        return selectUnfiltered(rand);
    }

    @Override
    public int getValueCount(){
        return this.valueCount;
    }

    private E selectUnfiltered(double rand) {
        int idx = Arrays.binarySearch(cumulative, rand);
        if (idx < 0) idx = -idx - 1;
        return items.get(idx);
    }

    public WeightedEnumSelectorImpl<E> withFilter(SelectionFilter filter) {
        if (filter == null || filter.isEmpty()) {
            return this;
        }
        return filteredCache.computeIfAbsent(filter, this::buildFilteredSelector);
    }

    private WeightedEnumSelectorImpl<E> buildFilteredSelector(SelectionFilter filter) {
        Map<E, Double> filtered = EntryFilter.apply(dataset, filter, field);
        if (filtered.isEmpty()) {
            LOGGER.trace("Filtered dataset map empty for filter {}", filter);
            return new WeightedEnumSelectorImpl<>(enumSet, field); // fallback to original
        }
        LOGGER.trace("Filtered selector: original={}, filtered={}, filter={}",
                dataset.size(), filtered.size(), filter);
        EnumSet<E> filteredEnumSet = EnumSet.copyOf(filtered.keySet());
        return new WeightedEnumSelectorImpl<>(filteredEnumSet, field);
    }

    private Weights<E> calcWeights(EnumSet<E> enumSet) {
        int size = enumSet.size();
        List<E> vals = new ArrayList<>(size);
        double[] cumulative = new double[size];
        double running = 0.0;
        int i = 0;
        for (E entry : enumSet) {
            double w = entry.getWeight();
            if (w <= 0.0) w = 0.0001;
            vals.add(entry);
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
