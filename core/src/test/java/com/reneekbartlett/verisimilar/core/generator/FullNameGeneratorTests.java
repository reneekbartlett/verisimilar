package com.reneekbartlett.verisimilar.core.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.FullName;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.selector.engine.FirstNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.LastNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.MiddleNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class FullNameGeneratorTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(FullNameGeneratorTests.class);

    @Test
    public void GenerateFullName_RandomGender() {
        DatasetResolverRegistry resolvers = TestUtils.getNameDatasetResolverRegistry();
        FirstNameSelectionEngine firstNameSelector = new FirstNameSelectionEngine(resolvers, TestUtils.WEIGHTED_RANDOM);
        MiddleNameSelectionEngine middleNameSelector = new MiddleNameSelectionEngine(resolvers, TestUtils.WEIGHTED_RANDOM);
        LastNameSelectionEngine lastNameSelector = new LastNameSelectionEngine(resolvers, TestUtils.WEIGHTED_RANDOM);

        FullNameGenerator fullNameGenerator = new FullNameGenerator(firstNameSelector, middleNameSelector, lastNameSelector);

        FullName fullName = fullNameGenerator.generate();
        LOGGER.debug("fullName=" + fullName.toString());
        Assertions.assertNotNull(fullName);

        Assertions.assertTrue(fullName.firstName() != fullName.middleName());
    }

    @Test
    public void GenerateFullName_Female() {
        DatasetResolverRegistry resolvers = TestUtils.getNameDatasetResolverRegistry();
        FirstNameSelectionEngine firstNameSelector = new FirstNameSelectionEngine(resolvers);
        MiddleNameSelectionEngine middleNameSelector = new MiddleNameSelectionEngine(resolvers);
        LastNameSelectionEngine lastNameSelector = new LastNameSelectionEngine(resolvers);

        FullNameGenerator fullNameGenerator = new FullNameGenerator(firstNameSelector, middleNameSelector, lastNameSelector);

        SelectionFilter filter = SelectionFilter.builder().gender(GenderIdentity.FEMALE).build();
        FullName fullNameFemale = fullNameGenerator.generate(filter);

        LOGGER.debug("fullNameFemale=" + fullNameFemale.toString());
        Assertions.assertNotNull(fullNameFemale);
    }

    @Test
    public void GenerateFullName_Male() {
        DatasetResolverRegistry resolvers = TestUtils.getNameDatasetResolverRegistry();

        FirstNameSelectionEngine firstNameSelector = new FirstNameSelectionEngine(resolvers);
        MiddleNameSelectionEngine middleNameSelector = new MiddleNameSelectionEngine(resolvers);
        LastNameSelectionEngine lastNameSelector = new LastNameSelectionEngine(resolvers);

        FullNameGenerator fullNameGenerator = new FullNameGenerator(firstNameSelector, middleNameSelector, lastNameSelector);

        //DatasetResolutionContext ctx = DatasetResolutionContext.builder().gender(GenderIdentity.MALE).build();
        SelectionFilter filter = SelectionFilter.builder().gender(GenderIdentity.MALE).build();
        FullName fullNameMale = fullNameGenerator.generate(filter);
        LOGGER.debug("fullNameMale=" + fullNameMale.toString());
        Assertions.assertNotNull(fullNameMale);
    }

    @Test
    public void GeneratedFullName_EthnicityFilter(){
        DatasetResolverRegistry resolvers = TestUtils.getNameDatasetResolverRegistry();

        FirstNameSelectionEngine firstNameSelector = new FirstNameSelectionEngine(resolvers);
        MiddleNameSelectionEngine middleNameSelector = new MiddleNameSelectionEngine(resolvers);
        LastNameSelectionEngine lastNameSelector = new LastNameSelectionEngine(resolvers);

        FullNameGenerator fullNameGenerator = new FullNameGenerator(firstNameSelector, middleNameSelector, lastNameSelector);

        SelectionFilter filter = SelectionFilter.builder().ethnicity(Ethnicity.INDIAN).build();
        FullName fullName = fullNameGenerator.generate(filter);

        LOGGER.debug("fullName=" + fullName.toString());
        Assertions.assertNotNull(fullName);
    }
}
