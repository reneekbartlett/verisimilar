package com.reneekbartlett.verisimilar.core.selector;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class SqliteSelectorImplTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqliteSelectorImplTests.class);

    private TemplateField field;

    private volatile SelectionFilter filter;

    @BeforeEach
    void setup() {
        field = TemplateField.STREET_NAME; // or mock if needed
    }

    //@Test
    void testSelectReturnsItem() {
        SqliteSelectorImpl<String> selector = new SqliteSelectorImpl<>(field, String.class);

        String result = selector.select();

        LOGGER.debug(result);
        //assertTrue(items.contains(result), () -> "Selected value '" + result + "' not found in dataset.");
    }

    @Test
    void testSelectReturnsItem_withFilter() {
        SqliteSelectorImpl<String> selector = new SqliteSelectorImpl<>(field, String.class);
        SelectionFilter filter = SelectionFilter.builder()
                //.startsWith("Ren", field)
                .addFilter("ZASDFDS", field, "endswith")
                //.streetName("WESTCHESTER") // adds TemplateField to equalToMap
                .addFilter("WEST", field, "contains")
                .build();
        selector.setFilter(filter);
        String result = selector.select();

        LOGGER.debug(result);
        //assertTrue(items.contains(result), () -> "Selected value '" + result + "' not found in dataset.");
    }

    //@Test
    void testConcurrentSelectIsSafe() throws InterruptedException {
        SqliteSelectorImpl<String> selector = new SqliteSelectorImpl<>(field, String.class);

        //SelectionFilter filter = SelectionFilter.builder().startsWith("A", field).build();
        //selector.setFilter(filter); // optional

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
