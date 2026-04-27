package com.reneekbartlett.verisimilar.core.generator;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.KeywordSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;
import com.reneekbartlett.verisimilar.core.templates.TemplateRegistry;
import com.reneekbartlett.verisimilar.core.templates.loader.TemplateRegistryLoader;

public class UsernameGeneratorTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsernameGeneratorTests.class);

    //@Test
    //@Disabled
    public void GenerateUsername_Random() {

        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();

        TemplateRegistryLoader templateLoader = new TemplateRegistryLoader();
        TemplateRegistry templateRegistry = templateLoader.loadFromClasspath("templates/username-templates.yaml");

        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM, templateRegistry);
        KeywordSelectionEngine keywordSelector = new KeywordSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM);

        UsernameGenerator usernameGenerator = new UsernameGenerator(usernameSelector, keywordSelector);
        String username1 = usernameGenerator.generate();

        LOGGER.debug("username1="+username1);
    }

    //@Test
    //@Disabled
    public void GenerateUsername_WithFullName() {

        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();
        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM, TestUtils.getUsernameTemplateRegistry());
        KeywordSelectionEngine keywordSelector = new KeywordSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM);

        UsernameGenerator usernameGenerator = new UsernameGenerator(usernameSelector, keywordSelector);
        String username1 = usernameGenerator.generate();

        LOGGER.debug("username1="+username1);
    }

    //@Test
    //@Disabled
    public void GenerateUsername_WithCriteria_FirstName() {

        TemplateRegistryLoader loader = new TemplateRegistryLoader();
        TemplateRegistry templateRegistry = loader.loadFromClasspath("templates/username-templates.yaml");

        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();
        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM, templateRegistry);
        KeywordSelectionEngine keywordSelector = new KeywordSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM);

        // TODO
        DatasetResolutionContext cxt = DatasetResolutionContext.builder().build();
        SelectionFilter criteria = SelectionFilter.builder().firstName("RENEE").build();

        UsernameGenerator usernameGenerator = new UsernameGenerator(usernameSelector, keywordSelector);
        String username1 = usernameGenerator.generate(cxt, criteria);

        LOGGER.debug("username1="+username1);
    }

    @Test
    //@Disabled
    public void GenerateUsername_WithCriteria() {

        TemplateRegistryLoader loader = new TemplateRegistryLoader();
        TemplateRegistry templateRegistry = loader.loadFromClasspath("templates/username-templates.yaml");

        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();
        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM, templateRegistry);
        KeywordSelectionEngine keywordSelector = new KeywordSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM);

        // TODO
        DatasetResolutionContext cxt = DatasetResolutionContext.builder().build();
        SelectionFilter filter = SelectionFilter.builder().firstName("RENEE").middleName("KATHRYN").lastName("BARTLETT")
                .birthday(LocalDate.of(1985, 8, 16)).build();

        UsernameGenerator usernameGenerator = new UsernameGenerator(usernameSelector, keywordSelector);
        String username1 = usernameGenerator.generate(cxt, filter);
        LOGGER.debug("username1="+username1);

        String username2 = usernameGenerator.generate(cxt, filter);
        LOGGER.debug("username2="+username2);

        String username3 = usernameGenerator.generate(cxt, filter);
        LOGGER.debug("username3="+username3);
    }
}
