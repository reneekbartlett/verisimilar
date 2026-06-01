package com.reneekbartlett.verisimilar.core.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.EmailAddressRecord;
import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.KeywordSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class EmailAddressGeneratorTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailAddressGeneratorTests.class);

    @Test
    public void GenerateEmailAddress_Any() {
        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();

        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers);
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(resolvers);
        KeywordSelectionEngine keywordSelector = new KeywordSelectionEngine(resolvers);

        EmailAddressGenerator emailAddressGenerator = new EmailAddressGenerator(usernameSelector, domainSelector, keywordSelector);

        EmailAddressRecord emailAddress1 = emailAddressGenerator.generate();
        LOGGER.debug("emailAddress1=" + emailAddress1.email());
    }

    @Test
    public void GenerateEmailAddress_FirstName() {
        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();

        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers);
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(resolvers);
        KeywordSelectionEngine keywordSelector = new KeywordSelectionEngine(resolvers);

        EmailAddressGenerator emailAddressGenerator = new EmailAddressGenerator(usernameSelector, domainSelector, keywordSelector);

        SelectionFilter filter = SelectionFilter.builder().firstName("Chip").build();
        EmailAddressRecord emailAddress1 = emailAddressGenerator.generate(filter);
        LOGGER.debug("emailAddress1=" + emailAddress1.email());
    }

    @Test
    public void GenerateEmailAddress_FirstName_LastName() {
        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();

        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers);
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(resolvers);
        KeywordSelectionEngine keywordSelector = new KeywordSelectionEngine(resolvers);

        EmailAddressGenerator emailAddressGenerator = new EmailAddressGenerator(usernameSelector, domainSelector, keywordSelector);

        SelectionFilter filter = SelectionFilter.builder().firstName("CHIP").lastName("SMITH").build();
        EmailAddressRecord emailAddress1 = emailAddressGenerator.generate(filter);
        LOGGER.debug("emailAddress1=" + emailAddress1.email());
    }

    @Test
    public void GenerateEmailAddress_Domain() {
        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();

        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers);
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(resolvers);
        KeywordSelectionEngine keywordSelector = new KeywordSelectionEngine(resolvers);

        EmailAddressGenerator emailAddressGenerator = new EmailAddressGenerator(usernameSelector, domainSelector, keywordSelector);

        SelectionFilter filter = SelectionFilter.builder().firstName("CHIP").lastName("SMITH")
                .domain("CHEWY.COM")
                .build();
        EmailAddressRecord emailAddress1 = emailAddressGenerator.generate(filter);
        LOGGER.debug("emailAddress1=" + emailAddress1.email());

        Assertions.assertTrue(emailAddress1.domain().equalsIgnoreCase("CHEWY.COM"));
    }

    @Test
    public void GenerateEmailAddress_Username() {
        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();

        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers);
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(resolvers);
        KeywordSelectionEngine keywordSelector = new KeywordSelectionEngine(resolvers);

        EmailAddressGenerator emailAddressGenerator = new EmailAddressGenerator(usernameSelector, domainSelector, keywordSelector);

        SelectionFilter filter = SelectionFilter.builder().firstName("CHIP").lastName("SMITH")
                .username("CHIPPYCHIPPY123")
                .build();
        EmailAddressRecord emailAddress1 = emailAddressGenerator.generate(filter);
        LOGGER.debug("emailAddress1=" + emailAddress1.email());

        Assertions.assertTrue(emailAddress1.username().equalsIgnoreCase("CHIPPYCHIPPY123"));
    }

    @Test
    public void GenerateEmailAddress_DomainType() {
        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();

        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers);
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(resolvers);
        KeywordSelectionEngine keywordSelector = new KeywordSelectionEngine(resolvers);

        EmailAddressGenerator emailAddressGenerator = new EmailAddressGenerator(usernameSelector, domainSelector, keywordSelector);

        SelectionFilter filter = SelectionFilter.builder()
                .domainType(DomainType.GOV)
                .build();
        EmailAddressRecord emailAddress1 = emailAddressGenerator.generate(filter);
        LOGGER.debug("emailAddress1=" + emailAddress1.email());

        Assertions.assertTrue(emailAddress1.type().equals(DomainType.GOV));
    }
}
