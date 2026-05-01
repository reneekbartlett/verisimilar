package com.reneekbartlett.verisimilar.core.generator;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.model.PersonRecord;

public class AsyncPersonGeneratorTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncPersonGeneratorTests.class);

    //private final Executor executor;

    public AsyncPersonGeneratorTests() {
        //this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Test
    public void GeneratePersonAsync_Test() {
        AsyncPersonGenerator generator = new AsyncPersonGenerator(TestUtils.getDatasetSelectionEngineRegistry());
        PersonRecord person = generator.generate();
        LOGGER.debug("Generated person: {}", person);
    }
}
