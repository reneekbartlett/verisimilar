package com.reneekbartlett.verisimilar.core.generator;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.model.EmailAddressRecord;
import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.KeywordSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.templates.TemplateRegistry;
import com.reneekbartlett.verisimilar.core.templates.loader.TemplateRegistryLoader;

public class EmailAddressGeneratorTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailAddressGeneratorTests.class);

    //@Test
    //@Disabled
    public void GenerateEmailAddress_Any() {

        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();
        //Set<String> templates
        
        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM, TestUtils.getUsernameTemplateRegistry());
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(resolvers, TestUtils.WEIGHTED_RANDOM);
        KeywordSelectionEngine keywordSelector = new KeywordSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM);

        EmailAddressGenerator emailAddressGenerator = new EmailAddressGenerator(usernameSelector, domainSelector, keywordSelector);

        EmailAddressRecord emailAddress1 = emailAddressGenerator.generate();
        LOGGER.debug("emailAddress1=" + emailAddress1.email());
    }

    //@Test
    //@Disabled
    public void GenerateEmailAddress_FirstName() {

        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();
        //Set<String> templates

        TemplateRegistryLoader templateLoader = new TemplateRegistryLoader();
        TemplateRegistry templateRegistry = templateLoader.loadFromClasspath("templates/username-templates.yaml");

        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM, templateRegistry);
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(resolvers, TestUtils.WEIGHTED_RANDOM);
        KeywordSelectionEngine keywordSelector = new KeywordSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM);

        EmailAddressGenerator emailAddressGenerator = new EmailAddressGenerator(usernameSelector, domainSelector, keywordSelector);

        SelectionFilter filter = SelectionFilter.builder().firstName("Cora").build();
        EmailAddressRecord emailAddress1 = emailAddressGenerator.generate(filter);
        LOGGER.debug("emailAddress1=" + emailAddress1.email());
    }

    @Test
    //@Disabled
    public void GenerateEmailAddress_FirstName_LastName() {

        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();
        //Set<String> templates

        TemplateRegistryLoader templateLoader = new TemplateRegistryLoader();
        TemplateRegistry templateRegistry = templateLoader.loadFromClasspath("templates/username-templates.yaml");

        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM, templateRegistry);
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(resolvers, TestUtils.WEIGHTED_RANDOM);
        KeywordSelectionEngine keywordSelector = new KeywordSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM);

        EmailAddressGenerator emailAddressGenerator = new EmailAddressGenerator(usernameSelector, domainSelector, keywordSelector);

        SelectionFilter filter = SelectionFilter.builder().firstName("CORA").lastName("BARTLETT").build();
        EmailAddressRecord emailAddress1 = emailAddressGenerator.generate(filter);
        LOGGER.debug("emailAddress1=" + emailAddress1.email());
    }
}
