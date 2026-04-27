package com.reneekbartlett.verisimilar.core.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.FirstNameSelectionEngine;

public class FirstNameGeneratorTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirstNameGeneratorTests.class);

    @Test
    public void GeneratedFirstName_ShouldNotBeNull() {
        FirstNameSelectionEngine firstNameProvider = new FirstNameSelectionEngine(TestUtils.getDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        //FirstNameSelectionEngine firstNameProvider = new FirstNameSelectorConfig(TestUtils.getDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM).build();
        FirstNameGenerator firstNameGenerator = new FirstNameGenerator(firstNameProvider);

        String firstName = firstNameGenerator.generate();
        LOGGER.debug("firstName="+firstName);
        Assertions.assertNotNull(firstName);
    }

    @Test
    public void GeneratedFirstName_FemaleCriteria_ShouldBeFemale() {
        FirstNameSelectionEngine firstNameProvider = new FirstNameSelectionEngine(TestUtils.getDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        //FirstNameSelectionEngine firstNameProvider = new FirstNameSelectorConfig(TestUtils.getDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM).build();
        FirstNameGenerator firstNameGenerator = new FirstNameGenerator(firstNameProvider);

        DatasetResolutionContext ctx = DatasetResolutionContext.builder().gender(GenderIdentity.FEMALE).build();
        SelectionFilter criteria = SelectionFilter.builder().gender(GenderIdentity.FEMALE).build();

        String femaleName = firstNameGenerator.generate(ctx, criteria);
        LOGGER.debug("femaleName="+femaleName);
        Assertions.assertNotNull(femaleName);
    }

    @Test
    public void GeneratedFirstName_MaleCriteria_ShouldBeMale() {
        FirstNameSelectionEngine firstNameProvider = new FirstNameSelectionEngine(TestUtils.getDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        //FirstNameSelectionEngine firstNameProvider = new FirstNameSelectorConfig(TestUtils.getDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM).build();
        FirstNameGenerator firstNameGenerator = new FirstNameGenerator(firstNameProvider);

        //DatasetResolutionContext ctx = DatasetResolutionContext.builder().gender(GenderIdentity.MALE).build();
        SelectionFilter criteria = SelectionFilter.builder().gender(GenderIdentity.MALE).build();

        String maleName = firstNameGenerator.generate(criteria);
        LOGGER.debug("maleName="+maleName);
        Assertions.assertNotNull(maleName);
    }
}
