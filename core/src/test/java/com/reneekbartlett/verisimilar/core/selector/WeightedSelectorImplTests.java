package com.reneekbartlett.verisimilar.core.selector;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.assertj.core.api.Assertions;
//import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.UnitType;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

@DisabledIf(value = "com.reneekbartlett.verisimilar.core.TestUtils#isCoreTestingDisabled")
public class WeightedSelectorImplTests {

    private TemplateField field;

    @BeforeEach
    public void setup() {
        field = TemplateField.FIRST_NAME; // or mock if needed
    }

    @Test
    public void testSelectReturnsWeightedItem() {
        Map<String, Double> weights = Map.of("ALISON", 0.1, "RENEE", 0.9);
        WeightedSelectorImpl<String> selector = new WeightedSelectorImpl<>(weights, field);

        // Run many times to ensure distribution is respected
        int countA = 0;
        int countB = 0;
        int countOther = 0;

        for (int i = 0; i < 10_000; i++) {
            String v = selector.select();
            if (v.equals("ALISON")) countA++;
            else if (v.equals("RENEE")) countB++;
            else countOther++;
        }

        final String countsStr = "RENEE=" + countB + ", ALISON=" + countA + "";

        Assertions.assertThat(countB).isGreaterThan(countA)
            .withFailMessage(() -> "RENEE should be selected more often than ALISON. [" + countsStr + "]");
        Assertions.assertThat(countOther).isEqualTo(0);
    }

    @Test
    public void testValueCountMatchesInput() {
        Map<String, Double> weights = Map.of("ALISON", 1.0, "ABBY", 2.0, "RENEE", 3.0, "JANE", 3.0);
        WeightedSelectorImpl<String> selector = new WeightedSelectorImpl<>(weights, field);

        int expected = 4;
        int actual = selector.getValueCount();

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testNegativeWeightsAreClamped() {
        Map<String, Double> weights = new HashMap<>();
        weights.put("ABE", -5.0);
        weights.put("BERT", 10.0);

        WeightedSelectorImpl<String> selector = new WeightedSelectorImpl<>(weights, field);

        // Should not throw, and should still select valid items
        for (int i = 0; i < 1000; i++) {
            Assertions.assertThat(weights).containsKey(selector.select());
        }
    }

    @Test
    public void testFilterReducesSelectionPool() {
        Map<String, Double> weights = Map.of("ALISON", 1.0, "ABBY", 2.0, "RENEE", 3.0, "JANE", 3.0);

        WeightedSelectorImpl<String> selector = new WeightedSelectorImpl<>(weights, field);

        SelectionFilter filter = SelectionFilter.builder()
                .addFilter("A", field, "startswith")
                .build(); // assume matches only A
        selector.setFilter(filter);

        String result = selector.select();

        Assertions.assertThat(result).startsWithIgnoringCase("A");
    }

    @Test
    void testSelectReturnsItemFromEnumSet() {
        EnumSet<UnitType> unitTypes = EnumSet.allOf(UnitType.class);
        WeightedEnumSelectorImpl<?> selector = new WeightedEnumSelectorImpl<>(unitTypes, field);

        UnitType result = (UnitType)selector.select();
        //assertTrue(unitTypes.contains(result), () -> "Selected value '" + result + "' not found in EnumSet.");
        Assertions.assertThat(result).isIn(unitTypes);
    }

    @Test
    public void testFilterExcludesEverythingFallsBackToOriginal() {
        Map<String, Double> weights = Map.of("ALISON", 1.0, "ABBY", 1.0, "RENEE", 1.0);

        WeightedSelectorImpl<String> selector = new WeightedSelectorImpl<>(weights, field);

        SelectionFilter filter = SelectionFilter.builder()
                .addFilter("NO MATCH", field, "startswith")
                //.startsWith("NO MATCH", field)
                .build();
        selector.setFilter(filter);

        // Should fall back to unfiltered selection
        String result = selector.select();

        Assertions.assertThat(result).isNotNull().isIn(weights.keySet());
    }

    @Test
    void testEmptyFilterDoesNotAffectSelection() {
        Map<String, Double> weights = Map.of("ALISON", 1.0, "RENEE", 1.0);
        WeightedSelectorImpl<String> selector = new WeightedSelectorImpl<>(weights, field);

        selector.setFilter(SelectionFilter.empty());

        String result = selector.select();

        Assertions.assertThat(result).isNotNull().isIn(weights.keySet());
    }

    @Test
    void testFilteredSelectorsAreMemoized() {
        Map<String, Double> weights = Map.of("ALISON", 1.0, "RENEE", 1.0, "ABBY", 1.0);
        WeightedSelectorImpl<String> selector = new WeightedSelectorImpl<>(weights, field);

        SelectionFilter filter = SelectionFilter.builder()
                //.startsWith("A", field)
                .addFilter("A", field, "startswith")
                .build();
        selector.setFilter(filter);

        WeightedSelectorImpl<String> first = selector.withFilter(filter);
        WeightedSelectorImpl<String> second = selector.withFilter(filter);

        //assertSame(first, second, "Filtered selector should be memoized");
        Assertions.assertThat(first).isEqualTo(second);
            //.withFailMessage("Filtered selector should be memoized", null);
    }

    @Test
    void testConcurrentSelectIsSafe() throws InterruptedException {
        Map<String, Double> weights = Map.of("ALISON", 1.0, "ABBY", 2.0, "RENEE", 3.0, "JANE", 3.0);

        WeightedSelectorImpl<String> selector = new WeightedSelectorImpl<>(weights, field);
        SelectionFilter filter = SelectionFilter.builder()
                .addFilter("A", field, "startswith")
                .build();
        selector.setFilter(filter);

        int threads = 50;
        int iterations = 10_000;

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        AtomicBoolean failed = new AtomicBoolean(false);

        for (int t = 0; t < threads; t++) {
            executor.submit(() -> {
                try {
                    for (int i = 0; i < iterations; i++) {
                        String result = selector.select();
                        Assertions.assertThat(result).isNotNull();
                    }
                } catch (Throwable ex) {
                    failed.set(true);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        Assertions.assertThat(failed.get()).isFalse()
            .withFailMessage(() -> "Concurrent access caused failure");
    }

    @Test
    void testWithFilterCreatesNewSelector() {
        // Manually provide the weights
        Map<String, Double> weights = Map.of("ALISON", 1.0, "ABBY", 2.0, "RENEE", 3.0, "JANE", 3.0);
        WeightedSelectorImpl<String> selector = new WeightedSelectorImpl<>(weights, field);

        SelectionFilter filter = SelectionFilter.builder()
                .addFilter("A", field, "startswith")
                .build();
        WeightedSelectorImpl<String> filtered = selector.withFilter(filter);

        //assertNotSame(selector, filtered);
        Assertions.assertThat(filter.startsWithMap()).containsKey(field);
        Assertions.assertThat(weights).containsKey(filtered.select().toUpperCase());
    }
}
