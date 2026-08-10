package com.reneekbartlett.verisimilar.core.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.model.DomainRecord;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;

public class DomainRecordGeneratorTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainRecordGeneratorTests.class);

    @Test
    public void GenerateDomain_Random() {
        DomainSelectionEngine domainProvider = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainRecordGenerator domainGenerator = new DomainRecordGenerator(domainProvider);

        DomainRecord domain1 = domainGenerator.generate();
        Assertions.assertNotNull(domain1);
        LOGGER.debug("domain1=" + domain1);
    }

    @Test
    public void GenerateDomain_Random_Multi() {
        DomainSelectionEngine domainProvider = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainRecordGenerator domainGenerator = new DomainRecordGenerator(domainProvider);

        DomainRecord domain1 = domainGenerator.generate();
        LOGGER.debug("domain1=" + domain1);

        DomainRecord domain2 = domainGenerator.generate();
        LOGGER.debug("domain2=" + domain2);

        DomainRecord domain3 = domainGenerator.generate();
        LOGGER.debug("domain3=" + domain3);
    }

    @Test
    public void GenerateDomain_B2C() {
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainRecordGenerator domainGenerator = new DomainRecordGenerator(domainSelector);

        SelectionFilter filter1 = SelectionFilter.builder().build();
        DomainRecord domain1 = domainGenerator.generate(filter1);
        LOGGER.debug("domain1=" + domain1);
        Assertions.assertNotNull(domain1);

        SelectionFilter filter2 = SelectionFilter.builder().startsWith("gma", TemplateField.DOMAIN).build();
        DomainRecord domain2 = domainGenerator.generate(filter2);
        LOGGER.debug("domain2=" + domain2);
        Assertions.assertNotNull(domain2);
        Assertions.assertTrue(domain2.domain().toUpperCase().startsWith("GMA")); // case-sensitive
    }

    @Test
    public void GenerateDomain_GOV() {
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainRecordGenerator domainGenerator = new DomainRecordGenerator(domainSelector);

        SelectionFilter filter = SelectionFilter.builder()
                .domainType(DomainType.GOV)
                .build();
        DomainRecord domain1 = domainGenerator.generate(filter);
        LOGGER.debug("domain1=" + domain1);
        Assertions.assertNotNull(domain1);
        Assertions.assertTrue(domain1.domain().toUpperCase().endsWith(".GOV"));
    }

    @Test
    public void GenerateDomain_GOV_EndsWith() {
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainRecordGenerator domainGenerator = new DomainRecordGenerator(domainSelector);

        SelectionFilter filter = SelectionFilter.builder()
                .domainType(DomainType.GOV)
                .startsWith("S", TemplateField.DOMAIN)
                .build();
        DomainRecord domain1 = domainGenerator.generate(filter);
        LOGGER.debug("domain1=" + domain1);
        Assertions.assertNotNull(domain1);
        Assertions.assertTrue(domain1.domain().toUpperCase().endsWith(".GOV"));
        Assertions.assertTrue(domain1.domain().toUpperCase().startsWith("S"));
    }

    // TODO:  Figure out how to handle situations filter doesnt return result (ie. domain starting with C?)
    @Test
    public void GenerateDomain_StartsWith() {
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainRecordGenerator domainGenerator = new DomainRecordGenerator(domainSelector);

        SelectionFilter filter = SelectionFilter.builder()
                .startsWith("H", TemplateField.DOMAIN)
                .build();
        DomainRecord domain1 = domainGenerator.generate(filter);
        LOGGER.debug("domain1=" + domain1);
        Assertions.assertNotNull(domain1);

        // TODO:  Fix me
        Assertions.assertTrue(domain1.domain().toUpperCase().startsWith("H"));
    }
}
