package com.reneekbartlett.verisimilar.core.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.MiddleNameSelectionEngine;

public class MiddleNameGeneratorTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(MiddleNameGeneratorTests.class);

    @Test
    public void GeneratedMiddleName_ShouldNotBeNull(){
        MiddleNameSelectionEngine middleNameProvider = new MiddleNameSelectionEngine(TestUtils.getNameDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        MiddleNameGenerator middleNameGenerator = new MiddleNameGenerator(middleNameProvider);

        String middleName = middleNameGenerator.generate();
        LOGGER.debug("middleName="+middleName);
        Assertions.assertNotNull(middleName);
    }

    @Test
    public void GeneratedMiddleName_FemaleCriteria_ShouldBeFemale() {
        MiddleNameSelectionEngine middleNameProvider = new MiddleNameSelectionEngine(TestUtils.getNameDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        MiddleNameGenerator middleNameGenerator = new MiddleNameGenerator(middleNameProvider);

        DatasetResolutionContext ctx = DatasetResolutionContext.builder().gender(GenderIdentity.FEMALE).build();
        SelectionFilter criteria = SelectionFilter.builder().gender(GenderIdentity.FEMALE).build();

        String femaleName = middleNameGenerator.generate(ctx, criteria);
        LOGGER.debug("femaleName="+femaleName);
        Assertions.assertNotNull(femaleName);
    }

    @Test
    public void GeneratedMiddleName_MaleCriteria_ShouldBeMale() {
        MiddleNameSelectionEngine middleNameProvider = new MiddleNameSelectionEngine(TestUtils.getNameDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        MiddleNameGenerator middleNameGenerator = new MiddleNameGenerator(middleNameProvider);

        DatasetResolutionContext ctx = DatasetResolutionContext.builder().gender(GenderIdentity.MALE).build();
        SelectionFilter criteria = SelectionFilter.builder().gender(GenderIdentity.MALE).build();

        String maleName = middleNameGenerator.generate(ctx, criteria);
        LOGGER.debug("maleName="+maleName);
        Assertions.assertNotNull(maleName);
    }
}
