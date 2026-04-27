package com.reneekbartlett.verisimilar.core.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
//import com.reneekbartlett.verisimilar.core.config.FirstNameSelectorConfig;
//import com.reneekbartlett.verisimilar.core.config.LastNameSelectorConfig;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.model.FullName;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.engine.FirstNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.LastNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.MiddleNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class FullNameGeneratorTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(FullNameGeneratorTests.class);

    //@Test
    public void GenerateFullName_RandomGender() {
        DatasetResolverRegistry resolvers = TestUtils.getNameDatasetResolverRegistry();
        FirstNameSelectionEngine firstNameProvider = new FirstNameSelectionEngine(resolvers, TestUtils.WEIGHTED_RANDOM);
        MiddleNameSelectionEngine middleNameProvider = new MiddleNameSelectionEngine(resolvers, TestUtils.WEIGHTED_RANDOM);

        LastNameSelectionEngine lastNameProvider = new LastNameSelectionEngine(resolvers, TestUtils.WEIGHTED_RANDOM);

        FullNameGenerator fullNameGenerator = new FullNameGenerator(firstNameProvider, middleNameProvider, lastNameProvider);

        FullName fullName = fullNameGenerator.generate();
        LOGGER.debug("fullName=" + fullName.toString());
        Assertions.assertNotNull(fullName);

        Assertions.assertTrue(fullName.firstName() != fullName.middleName());
    }

    //@Test
    public void GenerateFullName_Female() {
        DatasetResolverRegistry resolvers = TestUtils.getNameDatasetResolverRegistry();
        FirstNameSelectionEngine firstNameProvider = new FirstNameSelectionEngine(resolvers, TestUtils.WEIGHTED_RANDOM);
        MiddleNameSelectionEngine middleNameProvider = new MiddleNameSelectionEngine(resolvers, TestUtils.WEIGHTED_RANDOM);
        LastNameSelectionEngine lastNameProvider = new LastNameSelectionEngine(resolvers, TestUtils.WEIGHTED_RANDOM);

        FullNameGenerator fullNameGenerator = new FullNameGenerator(firstNameProvider, middleNameProvider, lastNameProvider);

        DatasetResolutionContext ctx = DatasetResolutionContext.builder().gender(GenderIdentity.FEMALE).build();
        SelectionFilter filter = SelectionFilter.builder().gender(GenderIdentity.FEMALE).build();
        FullName fullNameFemale = fullNameGenerator.generate(ctx, filter);
        LOGGER.debug("fullNameFemale=" + fullNameFemale.toString());
        Assertions.assertNotNull(fullNameFemale);
    }

    @Test
    public void GenerateFullName_Male() {
        DatasetResolverRegistry resolvers = TestUtils.getNameDatasetResolverRegistry();

        FirstNameSelectionEngine firstNameProvider = new FirstNameSelectionEngine(resolvers, TestUtils.WEIGHTED_RANDOM);
        //FirstNameSelectionEngine firstNameProvider = new FirstNameSelectorConfig(TestUtils.getDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM).build();

        MiddleNameSelectionEngine middleNameProvider = new MiddleNameSelectionEngine(resolvers, TestUtils.WEIGHTED_RANDOM);

        LastNameSelectionEngine lastNameProvider = new LastNameSelectionEngine(resolvers, TestUtils.WEIGHTED_RANDOM);
        //LastNameSelectionEngine lastNameProvider = new LastNameSelectorConfig(resolvers, TestUtils.WEIGHTED_RANDOM).build();

        FullNameGenerator fullNameGenerator = new FullNameGenerator(firstNameProvider, middleNameProvider, lastNameProvider);

        DatasetResolutionContext ctx = DatasetResolutionContext.builder().gender(GenderIdentity.MALE).build();
        SelectionFilter filter = SelectionFilter.builder().gender(GenderIdentity.MALE).build();
        FullName fullNameMale = fullNameGenerator.generate(ctx, filter);
        LOGGER.debug("fullNameMale=" + fullNameMale.toString());
        Assertions.assertNotNull(fullNameMale);
    }
}
