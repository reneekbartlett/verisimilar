package com.reneekbartlett.verisimilar.core.selector;

import org.junit.jupiter.api.Test;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.UnitType;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import org.junit.jupiter.api.BeforeEach;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class UniformSelectorImplTests {

    private TemplateField field;

    @BeforeEach
    void setup() {
        field = TemplateField.FIRST_NAME; // or mock if needed
    }

    @Test
    void testSelectReturnsItemFromList() {
        List<String> items = List.of("ALISON", "ABBY", "RENEE");
        UniformSelectorImpl<String> selector = new UniformSelectorImpl<>(items, field);

        String result = selector.select();
        assertTrue(items.contains(result), () -> "Selected value '" + result + "' not found in dataset.");
    }

    @Test
    void testSelectReturnsItemFromSet() {
        Set<String> items = Set.of("ALISON", "ABBY", "RENEE");
        UniformSelectorImpl<String> selector = new UniformSelectorImpl<>(items, field);

        String result = selector.select();
        assertTrue(items.contains(result), () -> "Selected value '" + result + "' not found in dataset.");
    }

    @Test
    void testSelectReturnsItemFromMapKeys() {
        Map<String, Double> weights = Map.of("ALISON", 1.0, "ABBY", 2.0, "RENEE", 2.0);
        UniformSelectorImpl<String> selector = new UniformSelectorImpl<>(weights, field);

        String result = selector.select();
        assertTrue(weights.containsKey(result), () -> "Selected value '" + result + "' not found in weight map");
    }

    @Test
    void testSelectReturnsItemFromEnumSet() {
        EnumSet<UnitType> unitTypes = EnumSet.allOf(UnitType.class);
        UniformSelectorImpl<UnitType> selector = new UniformSelectorImpl<>(unitTypes, field);

        UnitType result = selector.select();
        assertTrue(unitTypes.contains(result), () -> "Selected value '" + result + "' not found in EnumSet.");
    }

    @Test
    void testFilterReducesSelectionPool() {
        List<String> items = List.of("ALISON", "ABBY", "RENEE");
        UniformSelectorImpl<String> selector = new UniformSelectorImpl<>(items, field);

        SelectionFilter filter = SelectionFilter.builder()
                .addFilter("A", field, "startswith")
                .build();  // assume matches only "A"
        selector.setFilter(filter);

        String result = selector.select();
        assertTrue(result.startsWith("A"), () -> "Result should be start with A but was: " + result);
    }

    @Test
    void testFilterExcludesEverythingFallsBackToOriginal() {
        List<String> items = List.of("ALISON", "ABBY", "RENEE");
        UniformSelectorImpl<String> selector = new UniformSelectorImpl<>(items, field);

        SelectionFilter filter = SelectionFilter.builder()
                .addFilter("NO MATCH", field, "startswith")
                .build();
        selector.setFilter(filter);

        // Should fall back to un-filtered selection
        String result = selector.select();
        assertNotNull(result, "Selected value should not be null");
        assertTrue(items.contains(result), () -> "Selected value '" + result + "' not found in dataset.");
    }

    @Test
    void testEmptyFilterDoesNotAffectSelection() {
        List<String> items = List.of("ALISON", "ABBY", "RENEE");
        UniformSelectorImpl<String> selector = new UniformSelectorImpl<>(items, field);

        selector.setFilter(SelectionFilter.empty());

        String result = selector.select();
        assertNotNull(result, "Selected value should not be null");
        assertTrue(items.contains(result), () -> "Selected value '" + result + "' not found in dataset.");
    }

    @Test
    void testValueCount() {
        List<String> items = List.of("ALISON", "ABBY", "RENEE", "JANE");
        UniformSelectorImpl<String> selector = new UniformSelectorImpl<>(items, field);

        int expected = 4;
        int actual = selector.getValueCount();
        assertEquals(expected, actual, () -> "Expected value to be " + expected + " but was " + actual);
    }

    @Test
    void testConcurrentSelectIsSafe() throws InterruptedException {
        List<String> items = List.of("ALISON", "ABBY", "RENEE");
        UniformSelectorImpl<String> selector = new UniformSelectorImpl<>(items, field);

        SelectionFilter filter = SelectionFilter.builder()
                .addFilter("A", field, "startswith").build();
        selector.setFilter(filter); // optional

        int threads = 50;
        int iterations = 10_000;

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        AtomicBoolean failed = new AtomicBoolean(false);

        for (int t = 0; t < threads; t++) {
            executor.submit(() -> {
                try {
                    for (int i = 0; i < iterations; i++) {
                        String result = selector.select();
                        assertNotNull(result);
                    }
                } catch (Throwable ex) {
                    failed.set(true);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        assertFalse(failed.get(), "Concurrent access caused failure");
    }
}
