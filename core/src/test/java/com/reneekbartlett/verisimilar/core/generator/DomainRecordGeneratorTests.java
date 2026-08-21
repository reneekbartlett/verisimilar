package com.reneekbartlett.verisimilar.core.generator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

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

/***
 * DomainRecordGeneratorTests
 *  > DomainDatasetResolver
 */
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

        //SelectionFilter filter1 = SelectionFilter.builder().build();
        //DomainRecord domain1 = domainGenerator.generate(filter1);
        //LOGGER.debug("domain1=" + domain1);
        //assertThat(domain1).isNotNull();


        SelectionFilter filter2 = SelectionFilter.builder()
                .addFilter("G", TemplateField.DOMAIN, "startswith")
                .addFilter("b2c", TemplateField.DOMAIN_TYPE, "eq")
                .build();
        LOGGER.debug("filter2={}", filter2);
        // TODO: add assert for filter2?
        assertThat(filter2.startsWithMap()).containsKey(TemplateField.DOMAIN);
        assertThat(filter2.domainType().get()).isEqualTo(DomainType.B2C);

        DomainRecord domain2 = domainGenerator.generate(filter2);
        LOGGER.debug("domain2=" + domain2);

        assertThat(domain2.domain()).isNotNull();
        assertThat(domain2.domain()).startsWithIgnoringCase("g");
        assertThat(domain2.domainType()).isEqualTo(DomainType.B2C);
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
    public void GenerateDomain_GOV_startswith() {
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainRecordGenerator domainGenerator = new DomainRecordGenerator(domainSelector);

        SelectionFilter filter = SelectionFilter.builder()
                .domainType(DomainType.GOV)
                //.startsWith("S", TemplateField.DOMAIN)
                .addFilter("S", TemplateField.DOMAIN, "startswith")
                .build();
        DomainRecord domain1 = domainGenerator.generate(filter);
        LOGGER.debug("domain1=" + domain1);

        Assertions.assertNotNull(domain1);
        Assertions.assertTrue(domain1.domain().toUpperCase().endsWith(".GOV"));
        Assertions.assertTrue(domain1.domain().toUpperCase().startsWith("S"));
    }

    @Test
    public void GenerateDomain_B2C_B2B() {
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainRecordGenerator domainGenerator = new DomainRecordGenerator(domainSelector);

        // Examples: iname.net, icloud.it
        SelectionFilter filter = SelectionFilter.builder()
                .addFilter("i", TemplateField.DOMAIN, "startswith")
                .addFilter("t", TemplateField.DOMAIN, "endswith")
                .addFilter("b2b", TemplateField.DOMAIN_TYPE, "eq")
                .build();
        LOGGER.debug("filter={}", filter);
        assertThat(filter.startsWithMap()).containsKey(TemplateField.DOMAIN);

        assertThat(filter.domainType()).isPresent();
        assertThat(filter.domainType().get()).isEqualTo(DomainType.B2B);
        
        //assertThat(filter.domainTypes()).isPresent();
        //assertThat(filter.domainTypes().get()).hasSize(2);

        DomainRecord domainRecord = domainGenerator.generate(filter);
        LOGGER.debug("domain={}", domainRecord);

        assertThat(domainRecord.domain()).isNotNull();
        assertThat(domainRecord.domain()).startsWithIgnoringCase("i").endsWithIgnoringCase("t");
        assertThat(domainRecord.domainType()).isIn(Set.of(DomainType.B2C, DomainType.B2B));
    }

    @Test
    public void GenerateDomain_DomainType_Disposable() {
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainRecordGenerator domainGenerator = new DomainRecordGenerator(domainSelector);

        // Examples: iname.net, icloud.it
        SelectionFilter filter = SelectionFilter.builder()
                .addFilter("disposable", TemplateField.DOMAIN_TYPE, "eq")
                .build();
        LOGGER.debug("filter={}", filter);
        assertThat(filter.domainType()).isPresent();
        assertThat(filter.domainType().get()).isEqualTo(DomainType.DISPOSABLE);

        DomainRecord domainRecord = domainGenerator.generate(filter);
        LOGGER.debug("domain={}", domainRecord);

        assertThat(domainRecord.domain()).isNotNull();
        assertThat(domainRecord.domainType()).isIn(Set.of(DomainType.DISPOSABLE));
    }

    // TODO:  Figure out how to handle situations filter doesnt return result (ie. domain starting with C?)
    //@Test
    public void GenerateDomain_StartsWith() {
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainRecordGenerator domainGenerator = new DomainRecordGenerator(domainSelector);

        SelectionFilter filter = SelectionFilter.builder()
                //.startsWith("H", TemplateField.DOMAIN)
                .addFilter("H", TemplateField.DOMAIN, "startswith")
                .build();
        DomainRecord domain1 = domainGenerator.generate(filter);
        LOGGER.debug("domain1=" + domain1);
        Assertions.assertNotNull(domain1);

        // TODO:  Fix me
        Assertions.assertTrue(domain1.domain().toUpperCase().startsWith("H"));
    }
}
