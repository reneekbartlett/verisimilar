package com.reneekbartlett.verisimilar.core.selector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.EntryFilter;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public final class WeightedSelectorImpl<T> implements RandomSelector<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeightedSelectorImpl.class);

    private final Map<T, Double> weights;
    private final TemplateField field;
    private final List<T> items;
    private final double[] cumulative;
    private final int valueCount;

    private SelectionFilter filter;

    private record Weights(List<?> items, double[] itemWeights) {
        @Override
        public String toString() {
            return new StringBuilder(0).append("items.size:" + items.size())
                    .append(", itemWeights.length:" + itemWeights.length)
                    .toString();
        }
    }

    @SuppressWarnings("unchecked")
    public WeightedSelectorImpl(Map<T, Double> weights, TemplateField field) {
        Objects.requireNonNull(weights, "weights");
        if (weights.isEmpty()) {
            LOGGER.error("Weighted map cannot be empty");
            throw new IllegalArgumentException("Weighted map cannot be empty");
        }
        this.weights = weights;
        Weights w = calcWeights(weights);
        this.field = field;
        this.items = (List<T>) w.items;
        this.cumulative = w.itemWeights();
        this.valueCount = items.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T select() {
        // Random value in [0.0, 1.0)
        double rand = ThreadLocalRandom.current().nextDouble();
        if(filter !=null && !filter.isEmpty()) {
            Map<T, Double> filteredMap = EntryFilter.apply(this.weights, filter, field);
            LOGGER.debug("select (filtered) - original.size():{}, filteredMap.size():{}, filter:{}"
                    , this.weights.size(), filteredMap.size(), filter);
            if(filteredMap.size() > 0) {
                Weights filteredWeights = calcWeights(filteredMap);
                int idx = Arrays.binarySearch(filteredWeights.itemWeights, rand);
                if (idx < 0) idx = -idx - 1;
                return (T)filteredWeights.items.get(idx);
            }
        }
        int idx = Arrays.binarySearch(cumulative, rand);
        if (idx < 0) idx = -idx - 1;
        return items.get(idx);
    }

    @Override
    public int getValueCount(){
        return this.valueCount;
    }

    private Weights calcWeights(Map<T, Double> weights) {
        List<T> vals = new ArrayList<>();
        double[] c = new double[weights.size()];
        double running = 0.0;
        int i = 0;
        for (var entry : weights.entrySet()) {
            double w = entry.getValue();
            if (w < 0.0) w = 0.0001;
            vals.add(entry.getKey());
            running += w;
            c[i++] = running;
        }
        for (int j = 0; j < c.length; j++) {
            c[j] /= running;
        }
        return new Weights(vals,c);
    }

    public void setFilter(SelectionFilter filter) {
        this.filter = filter;
    }
}
