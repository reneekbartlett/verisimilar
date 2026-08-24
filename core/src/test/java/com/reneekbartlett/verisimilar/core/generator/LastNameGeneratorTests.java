package com.reneekbartlett.verisimilar.core.generator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.FilterOperator;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.engine.LastNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class LastNameGeneratorTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(LastNameGeneratorTests.class);

    //@Test
    public void GeneratedLastName_ShouldNotBeNull(){
        LastNameSelectionEngine lastNameProvider = new LastNameSelectionEngine(TestUtils.getNameDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        LastNameGenerator lastNameGenerator = new LastNameGenerator(lastNameProvider);

        String lastName = lastNameGenerator.generate();
        LOGGER.debug("lastName="+lastName);
        Assertions.assertThat(lastName).isNotNull();
    }

    @Test
    public void GeneratedLastName_ShouldNotBeNull_UniformRandom(){
        LastNameSelectionEngine lastNameProvider = new LastNameSelectionEngine(TestUtils.getNameDatasetResolverRegistry(), TestUtils.UNIFORM_RANDOM);
        LastNameGenerator lastNameGenerator = new LastNameGenerator(lastNameProvider);

        String lastName = lastNameGenerator.generate();
        LOGGER.debug("lastName="+lastName);
        Assertions.assertThat(lastName).isNotNull();
    }

    @Test
    public void GeneratedLastName_EthnicityFilter(){
        LastNameSelectionEngine lastNameProvider = new LastNameSelectionEngine(TestUtils.getNameDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        LastNameGenerator lastNameGenerator = new LastNameGenerator(lastNameProvider);

        SelectionFilter filter = SelectionFilter.builder()
                .ethnicity(Ethnicity.CHINESE).build();
        Assertions.assertThat(filter.ethnicity()).isPresent();
        Assertions.assertThat(filter.ethnicity().get()).isEqualTo(Ethnicity.CHINESE);

        String lastName = lastNameGenerator.generate(filter);

        LOGGER.debug("lastName="+lastName);
        Assertions.assertThat(lastName).isNotNull();
    }

    @Test
    public void GeneratedLastName_FilterEndsWith(){
        LastNameSelectionEngine lastNameProvider = new LastNameSelectionEngine(TestUtils.getNameDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        LastNameGenerator lastNameGenerator = new LastNameGenerator(lastNameProvider);

        SelectionFilter filter = SelectionFilter.builder()
                .addFilter("TT", TemplateField.LAST_NAME, FilterOperator.ENDS_WITH)
                .build();

        String lastName = lastNameGenerator.generate(filter);

        LOGGER.debug("lastName="+lastName);
        Assertions.assertThat(lastName).isNotNull();
        Assertions.assertThat(lastName).endsWith("TT");

    }
}
