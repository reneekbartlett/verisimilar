package com.reneekbartlett.verisimilar.core.selector;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.datasets.key.UsernameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.resolver.UsernameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.engine.KeywordSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.templates.TemplateRegistry;
import com.reneekbartlett.verisimilar.core.templates.loader.TemplateRegistryLoader;

/***
 * TODO: This won't populate the template placeholder values yet (thats done in UsernameGenerator ATM)
 */
public class KeywordSelectionEngineTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeywordSelectionEngineTests.class);

    //private DatasetResolverRegistry resolvers;
    //private UsernameDatasetResolver usernameDatasetResolver;
    //private UsernameSelectionEngine usernameSelector1;
    //private UsernameSelectionEngine usernameSelector2;

    public KeywordSelectionEngineTests() {
        //this.usernameSelector1 = new UsernameSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM, templateRegistry);
    }

    //@Test
    public void GenerateKeyword_Random() {
        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();

        KeywordSelectionEngine keywordSelector = new KeywordSelectionEngine(resolvers.keyword());

        String keyword1 = keywordSelector.select();
        LOGGER.debug("keyword1={}", keyword1);
    }

    @Test
    public void GenerateKeyword_Type2_Random() {
        //UsernameSelectionEngine(DatasetResolverRegistry resolvers, SelectorStrategy<String> strategy, TemplateRegistry templateRegistry)
        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();
        UsernameDatasetResolver usernameDatasetResolver = resolvers.username();

        //TemplateRegistryLoader templateLoader = new TemplateRegistryLoader();
        //TemplateRegistry templateRegistry = templateLoader.loadFromClasspath("templates/username-templates.yaml");

        //UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(usernameDatasetResolver, TestUtils.UNIFORM_RANDOM, templateRegistry);
        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(usernameDatasetResolver);

        String username1 = usernameSelector.select();
        LOGGER.debug("username1={}", username1);

        SelectionFilter filter = SelectionFilter.builder().startsWith("R", TemplateField.USERNAME).build();
        String username2 = usernameSelector.select(filter);
        LOGGER.debug("username2={}", username2);
    }

    public void GenerateUsername_Random() {
        //UsernameSelectionEngine(DatasetResolverRegistry resolvers, SelectorStrategy<String> strategy, TemplateRegistry templateRegistry)
        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();
        //UsernameDatasetResolver usernameDatasetResolver = resolvers.username();

        //TemplateRegistryLoader templateLoader = new TemplateRegistryLoader();
        //TemplateRegistry templateRegistry = templateLoader.loadFromClasspath("templates/username-templates.yaml");

        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers);
        //select(UsernameDatasetKey key, SelectionFilter filter)
        String username1 = usernameSelector.select();
        LOGGER.debug("username1={}", username1);

        UsernameDatasetKey key = UsernameDatasetKey.defaults();
        SelectionFilter filter = SelectionFilter.empty();
        String username2 = usernameSelector.select(key, filter);
        LOGGER.debug("username2={}", username2);
    }

}
