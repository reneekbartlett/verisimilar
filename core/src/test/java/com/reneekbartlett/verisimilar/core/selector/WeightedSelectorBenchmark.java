package com.reneekbartlett.verisimilar.core.selector;

import org.openjdk.jmh.annotations.*;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import java.util.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class WeightedSelectorBenchmark {

    @Param({"10", "100", "1000"})
    public int size;

    private WeightedSelectorImpl<String> selector;
    private WeightedSelectorImpl<String> filteredSelector;

    @Setup(Level.Trial)
    public void setup() {
        Map<String, Double> weights = new LinkedHashMap<>();
        for (int i = 0; i < size; i++) {
            weights.put("Item-" + i, (double) (i + 1));
        }

        TemplateField field = TemplateField.FIRST_NAME;
        selector = new WeightedSelectorImpl<>(weights, field);

        // Filter that matches only 1 item
        SelectionFilter filter = SelectionFilter.builder().startsWith("A", field).build(); // assume matches only A
        selector.setFilter(filter);

        // Pre-build filtered selector to measure memoized path
        filteredSelector = selector.withFilter(filter);
    }

    @Benchmark
    public String baselineSelect() {
        return selector.select();
    }

    @Benchmark
    public String filteredSelect() {
        return filteredSelector.select();
    }

    @Benchmark
    public String dynamicFilterSelect() {
        return selector.select(); // uses volatile filter + memoized filtered selector
    }

    @Benchmark
    @Threads(4)
    public String select4Threads() {
        return selector.select();
    }

    @Benchmark
    @Threads(8)
    public String select8Threads() {
        return selector.select();
    }

    @Benchmark
    @Threads(16)
    public String select16Threads() {
        return selector.select();
    }
}
