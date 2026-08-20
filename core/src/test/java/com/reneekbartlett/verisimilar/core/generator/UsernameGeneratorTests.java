package com.reneekbartlett.verisimilar.core.generator;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;

//@org.junit.jupiter.api.Disabled
@DisabledIf(value = "com.reneekbartlett.verisimilar.core.TestUtils#isCoreTestingDisabled")
public class UsernameGeneratorTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsernameGeneratorTests.class);

    @Test
    public void GenerateUsername_Random() {
        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();
        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers);
        UsernameGenerator usernameGenerator = new UsernameGenerator(usernameSelector);
        String username1 = usernameGenerator.generate();
        LOGGER.debug("username1="+username1);
        Assertions.assertFalse(username1.contains("{"));
    }

    @Test
    public void GenerateUsername_WithCriteria_FirstName() {
        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();
        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers);
        SelectionFilter filter = SelectionFilter.builder().firstName("RENEE").build();
        UsernameGenerator usernameGenerator = new UsernameGenerator(usernameSelector);
        String username1 = usernameGenerator.generate(filter);

        LOGGER.debug("username1="+username1);
        Assertions.assertFalse(username1.contains("{"));
    }

    @Test
    public void GenerateUsername_WithCriteria() {
        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();
        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers);
        SelectionFilter filter = SelectionFilter.builder().firstName("RENEE").middleName("K").lastName("BARTLETT")
                .birthday(LocalDate.of(1980, 7, 30)).build();

        UsernameGenerator usernameGenerator = new UsernameGenerator(usernameSelector);
        String username1 = usernameGenerator.generate(filter);
        LOGGER.debug("username1="+username1);
        Assertions.assertFalse(username1.contains("{"));

        String username2 = usernameGenerator.generate(filter);
        LOGGER.debug("username2="+username2);
        Assertions.assertFalse(username2.contains("{"));

        String username3 = usernameGenerator.generate(filter);
        LOGGER.debug("username3="+username3);
        Assertions.assertFalse(username3.contains("{"));
    }

    @Test
    public void GenerateUsername_Filter_EqualTo() {
        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();
        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers);

        SelectionFilter filter = SelectionFilter.builder()
                .birthday(LocalDate.of(1980, 7, 30))
                .username("hunnyb123")
                .build();

        UsernameGenerator usernameGenerator = new UsernameGenerator(usernameSelector);
        String username1 = usernameGenerator.generate(filter);
        LOGGER.debug("username1="+username1);
        Assertions.assertTrue(username1.equalsIgnoreCase("hunnyb123"));
    }

    @Test
    public void GenerateUsername_Filter_StartsWith() {
        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();
        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers);

        SelectionFilter filter = SelectionFilter.builder()
                .birthday(LocalDate.of(1980, 7, 30))
                //.startsWith("H", TemplateField.USERNAME)
                .addFilter("H", TemplateField.USERNAME, "startswith")
                .build();

        UsernameGenerator usernameGenerator = new UsernameGenerator(usernameSelector);
        String username1 = usernameGenerator.generate(filter);
        LOGGER.debug("username starts with H="+username1);
        Assertions.assertTrue(username1.startsWith("H"));
    }
}
