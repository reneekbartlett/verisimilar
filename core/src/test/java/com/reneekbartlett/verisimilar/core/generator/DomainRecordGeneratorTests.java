package com.reneekbartlett.verisimilar.core.generator;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.model.DomainRecord;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.FilterOperator;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

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
        LOGGER.debug("domain1=" + domain1);

        Assertions.assertThat(domain1).isNotNull();
    }

    @Test
    public void GenerateDomain_Random_Multi() {
        DomainSelectionEngine domainProvider = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainRecordGenerator domainGenerator = new DomainRecordGenerator(domainProvider);

        DomainRecord domain1 = domainGenerator.generate();
        LOGGER.debug("domain1=" + domain1);
        Assertions.assertThat(domain1).isNotNull();

        DomainRecord domain2 = domainGenerator.generate();
        LOGGER.debug("domain2=" + domain2);
        Assertions.assertThat(domain2).isNotNull();

        DomainRecord domain3 = domainGenerator.generate();
        LOGGER.debug("domain3=" + domain3);
        Assertions.assertThat(domain3).isNotNull();
    }

    @Test
    public void GenerateDomain_B2C() {
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainRecordGenerator domainGenerator = new DomainRecordGenerator(domainSelector);

        SelectionFilter filter2 = SelectionFilter.builder()
                .addFilter("G", TemplateField.DOMAIN, FilterOperator.STARTS_WITH)
                .addFilter("b2c", TemplateField.DOMAIN_TYPE, FilterOperator.EQUAL_TO)
                .build();
        LOGGER.debug("filter2={}", filter2);
        // TODO: add assert for filter2?
        Assertions.assertThat(filter2.startsWithMap()).containsKey(TemplateField.DOMAIN);
        Assertions.assertThat(filter2.domainType().get()).isEqualTo(DomainType.B2C);

        DomainRecord domain2 = domainGenerator.generate(filter2);
        LOGGER.debug("domain2=" + domain2);

        Assertions.assertThat(domain2.domain()).isNotNull();
        Assertions.assertThat(domain2.domain()).startsWithIgnoringCase("g");
        Assertions.assertThat(domain2.domainType()).isEqualTo(DomainType.B2C);
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
        Assertions.assertThat(domain1).isNotNull();
        Assertions.assertThat(domain1.domain()).endsWithIgnoringCase(".GOV");
    }

    @Test
    public void GenerateDomain_GOV_startswith() {
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainRecordGenerator domainGenerator = new DomainRecordGenerator(domainSelector);

        SelectionFilter filter = SelectionFilter.builder()
                .domainType(DomainType.GOV)
                //.startsWith("S", TemplateField.DOMAIN)
                .addFilter("S", TemplateField.DOMAIN, FilterOperator.STARTS_WITH)
                .build();
        DomainRecord domain1 = domainGenerator.generate(filter);
        LOGGER.debug("domain1=" + domain1);

        Assertions.assertThat(domain1).isNotNull();
        Assertions.assertThat(domain1.domain()).endsWithIgnoringCase(".GOV");
        Assertions.assertThat(domain1.domain()).startsWithIgnoringCase("S");
    }

    @Test
    public void GenerateDomain_B2C_B2B() {
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainRecordGenerator domainGenerator = new DomainRecordGenerator(domainSelector);

        // Examples: iname.net, icloud.it
        SelectionFilter filter = SelectionFilter.builder()
                .addFilter("i", TemplateField.DOMAIN, FilterOperator.STARTS_WITH)
                .addFilter("t", TemplateField.DOMAIN, FilterOperator.ENDS_WITH)
                .addFilter("b2b", TemplateField.DOMAIN_TYPE, FilterOperator.EQUAL_TO)
                .build();
        LOGGER.debug("filter={}", filter);
        Assertions.assertThat(filter.startsWithMap()).containsKey(TemplateField.DOMAIN);

        Assertions.assertThat(filter.domainType()).isPresent();
        Assertions.assertThat(filter.domainType().get()).isEqualTo(DomainType.B2B);

        //assertThat(filter.domainTypes()).isPresent();
        //assertThat(filter.domainTypes().get()).hasSize(2);

        DomainRecord domainRecord = domainGenerator.generate(filter);
        LOGGER.debug("domain={}", domainRecord);

        Assertions.assertThat(domainRecord.domain()).isNotNull();
        Assertions.assertThat(domainRecord.domain()).startsWithIgnoringCase("i").endsWithIgnoringCase("t");
        Assertions.assertThat(domainRecord.domainType()).isIn(Set.of(DomainType.B2C, DomainType.B2B));
    }

    @Test
    public void GenerateDomain_DomainType_Disposable() {
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainRecordGenerator domainGenerator = new DomainRecordGenerator(domainSelector);

        // Examples: iname.net, icloud.it
        SelectionFilter filter = SelectionFilter.builder()
                .addFilter("disposable", TemplateField.DOMAIN_TYPE, FilterOperator.EQUAL_TO)
                .build();
        LOGGER.debug("filter={}", filter);
        Assertions.assertThat(filter.domainType()).isPresent();
        Assertions.assertThat(filter.domainType().get()).isEqualTo(DomainType.DISPOSABLE);

        DomainRecord domainRecord = domainGenerator.generate(filter);
        LOGGER.debug("domain={}", domainRecord);

        Assertions.assertThat(domainRecord.domain()).isNotNull();
        Assertions.assertThat(domainRecord.domainType()).isIn(Set.of(DomainType.DISPOSABLE));
    }

    // TODO:  Figure out how to handle situations filter doesnt return result (ie. domain starting with C?)
    //@Test
    public void GenerateDomain_StartsWith() {
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(TestUtils.getEmailAddressDatasetResolverRegistry(), TestUtils.WEIGHTED_RANDOM);
        DomainRecordGenerator domainGenerator = new DomainRecordGenerator(domainSelector);

        SelectionFilter filter = SelectionFilter.builder()
                .addFilter("H", TemplateField.DOMAIN, FilterOperator.STARTS_WITH)
                .build();
        DomainRecord domain1 = domainGenerator.generate(filter);
        LOGGER.debug("domain1=" + domain1);
        Assertions.assertThat(domain1).isNotNull();

        // TODO:  Fix me
        Assertions.assertThat(domain1.domain()).startsWithIgnoringCase("H");
    }
}
