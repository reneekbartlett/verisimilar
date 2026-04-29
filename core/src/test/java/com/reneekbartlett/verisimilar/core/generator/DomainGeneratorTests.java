package com.reneekbartlett.verisimilar.core.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;

public class DomainGeneratorTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainGeneratorTests.class);

    //@Test
    public void GenerateDomain_Random() {
        DomainSelectionEngine domainProvider = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainGenerator domainGenerator = new DomainGenerator(domainProvider);

        String domain1 = domainGenerator.generate();
        LOGGER.debug("domain1=" + domain1);
    }

    //@Test
    public void GenerateDomain_Random_Multi() {
        DomainSelectionEngine domainProvider = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainGenerator domainGenerator = new DomainGenerator(domainProvider);

        String domain1 = domainGenerator.generate();
        LOGGER.debug("domain1=" + domain1);

        String domain2 = domainGenerator.generate();
        LOGGER.debug("domain2=" + domain2);

        String domain3 = domainGenerator.generate();
        LOGGER.debug("domain3=" + domain3);
    }

    @Test
    public void GenerateDomain_B2C() {
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainGenerator domainGenerator = new DomainGenerator(domainSelector);

        SelectionFilter filter1 = SelectionFilter.builder().build();
        String domain1 = domainGenerator.generate(filter1);
        LOGGER.debug("domain1=" + domain1);
        Assertions.assertNotNull(domain1);

        SelectionFilter filter2 = SelectionFilter.builder().startsWith("gma", TemplateField.DOMAIN).build();
        String domain2 = domainGenerator.generate(filter2);
        LOGGER.debug("domain2=" + domain2);
        Assertions.assertNotNull(domain2);
        Assertions.assertTrue(domain2.startsWith("GMA")); // case-sensitive
    }

    //@Test
    public void GenerateDomain_GOV() {
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainGenerator domainGenerator = new DomainGenerator(domainSelector);

        SelectionFilter filter = SelectionFilter.builder()
                .domainType(DomainType.GOV)
                .build();
        String domain1 = domainGenerator.generate(filter);
        LOGGER.debug("domain1=" + domain1);
        Assertions.assertNotNull(domain1);
        Assertions.assertTrue(domain1.endsWith(".GOV"));
    }

    //@Test
    public void GenerateDomain_GOV_EndsWith() {
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainGenerator domainGenerator = new DomainGenerator(domainSelector);

        SelectionFilter filter = SelectionFilter.builder()
                .domainType(DomainType.GOV)
                .startsWith("S", TemplateField.DOMAIN)
                .build();
        String domain1 = domainGenerator.generate(filter);
        LOGGER.debug("domain1=" + domain1);
        Assertions.assertNotNull(domain1);
        Assertions.assertTrue(domain1.endsWith(".GOV"));
        Assertions.assertTrue(domain1.startsWith("S"));
    }

    //@Test
    public void GenerateDomain_StartsWith() {
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainGenerator domainGenerator = new DomainGenerator(domainSelector);

        SelectionFilter filter = SelectionFilter.builder()
                .startsWith("C", TemplateField.DOMAIN)
                .build();
        String domain1 = domainGenerator.generate(filter);
        LOGGER.debug("domain1=" + domain1);
        Assertions.assertNotNull(domain1);
        Assertions.assertTrue(domain1.startsWith("C"));
    }
}
