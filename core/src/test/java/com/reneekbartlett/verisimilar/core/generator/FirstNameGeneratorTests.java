package com.reneekbartlett.verisimilar.core.generator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
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
        Assertions.assertThat(firstName).isNotNull();
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
        Assertions.assertThat(femaleName).isNotNull();
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
        Assertions.assertThat(maleName).isNotNull();
    }

    @Test
    public void GeneratedFirstName_FilterUnisex_ShouldBeUnisex() {
        FirstNameSelectionEngine firstNameProvider = new FirstNameSelectionEngine(TestUtils.getDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        FirstNameGenerator firstNameGenerator = new FirstNameGenerator(firstNameProvider);

        //DatasetResolutionContext ctx = DatasetResolutionContext.builder().gender(GenderIdentity.MALE).build();
        SelectionFilter filter = SelectionFilter.builder()
                .gender(GenderIdentity.GENDER_UNSPECIFIED)
                .addFilter("SKYLER", TemplateField.FIRST_NAME, "startswith")
                .build();
        Assertions.assertThat(filter.gender()).isPresent();
        Assertions.assertThat(filter.gender().get()).isEqualTo(GenderIdentity.GENDER_UNSPECIFIED);

        String unisexName = firstNameGenerator.generate(filter);
        LOGGER.debug("unisexName="+unisexName);
        Assertions.assertThat(unisexName).isNotNull();
        
        
    }
}
