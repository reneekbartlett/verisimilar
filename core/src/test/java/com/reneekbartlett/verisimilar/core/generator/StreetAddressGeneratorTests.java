package com.reneekbartlett.verisimilar.core.generator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.PostalAddressDatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.model.AddressLineTwo;
import com.reneekbartlett.verisimilar.core.model.StreetAddress;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.UnitType;
import com.reneekbartlett.verisimilar.core.selector.engine.AddressTwoSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetSuffixSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class StreetAddressGeneratorTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreetAddressGeneratorTests.class);

    @Test
    public void GenerateStreetAddress() {
        PostalAddressDatasetResolverRegistry registry = TestUtils.getPostalAddressDatasetResolverRegistry();

        StreetNameSelectionEngine streetNameSelector = new StreetNameSelectionEngine(registry, TestUtils.WEIGHTED_RANDOM);
        StreetSuffixSelectionEngine streetSuffixSelector = new StreetSuffixSelectionEngine(registry, TestUtils.WEIGHTED_RANDOM);
        AddressTwoSelectionEngine addressTwoSelector = new AddressTwoSelectionEngine(registry, TestUtils.WEIGHTED_RANDOM);

        StreetAddressGenerator streetAddressGenerator = new StreetAddressGenerator(
                streetNameSelector, streetSuffixSelector, addressTwoSelector);

        StreetAddress streetAddress1 = streetAddressGenerator.generate();
        LOGGER.debug(streetAddress1.toString());

    }

    @Test
    public void GenerateStreetAddress_WithFilter() {
        PostalAddressDatasetResolverRegistry registry = TestUtils.getPostalAddressDatasetResolverRegistry();

        StreetNameSelectionEngine streetNameSelector = new StreetNameSelectionEngine(registry, TestUtils.WEIGHTED_RANDOM);
        StreetSuffixSelectionEngine streetSuffixSelector = new StreetSuffixSelectionEngine(registry, TestUtils.WEIGHTED_RANDOM);
        AddressTwoSelectionEngine addressTwoSelector = new AddressTwoSelectionEngine(registry, TestUtils.WEIGHTED_RANDOM);

        StreetAddressGenerator streetAddressGenerator = new StreetAddressGenerator(
                streetNameSelector, streetSuffixSelector, addressTwoSelector);

        SelectionFilter filter = SelectionFilter.builder()
                .addFilter("MA", TemplateField.STREET_NAME, "startswith")
                .build();
        StreetAddress streetAddress1 = streetAddressGenerator.generate(filter);
        LOGGER.debug(streetAddress1.toString());

        Assertions.assertThat(streetAddress1.addressLineOne()).isNotNull();
    }

    @Test
    public void GenerateStreetAddress_WithFilter_POBOX() {
        PostalAddressDatasetResolverRegistry registry = TestUtils.getPostalAddressDatasetResolverRegistry();

        StreetNameSelectionEngine streetNameSelector = new StreetNameSelectionEngine(registry, TestUtils.WEIGHTED_RANDOM);
        StreetSuffixSelectionEngine streetSuffixSelector = new StreetSuffixSelectionEngine(registry, TestUtils.WEIGHTED_RANDOM);
        AddressTwoSelectionEngine addressTwoSelector = new AddressTwoSelectionEngine(registry, TestUtils.WEIGHTED_RANDOM);

        StreetAddressGenerator streetAddressGenerator = new StreetAddressGenerator(
                streetNameSelector, streetSuffixSelector, addressTwoSelector);

        SelectionFilter filter = SelectionFilter.builder()
                //.equalTo(AddressCategory.PO_BOX.getLabel(), TemplateField.ADDRESS_CATEGORY) // TODO
                .build();
        StreetAddress streetAddress1 = streetAddressGenerator.generate(filter);
        LOGGER.debug(streetAddress1.toString());
        Assertions.assertThat(streetAddress1.addressLineOne()).isNotNull();
        //Assertions.assertTrue(streetAddress1.address1());
    }

    @Test
    public void GenerateStreetAddress_WithFilter_Address2() {
        PostalAddressDatasetResolverRegistry registry = TestUtils.getPostalAddressDatasetResolverRegistry();

        StreetNameSelectionEngine streetNameSelector = new StreetNameSelectionEngine(registry, TestUtils.WEIGHTED_RANDOM);
        StreetSuffixSelectionEngine streetSuffixSelector = new StreetSuffixSelectionEngine(registry, TestUtils.WEIGHTED_RANDOM);
        AddressTwoSelectionEngine addressTwoSelector = new AddressTwoSelectionEngine(registry, TestUtils.WEIGHTED_RANDOM);

        StreetAddressGenerator streetAddressGenerator = new StreetAddressGenerator(
                streetNameSelector, streetSuffixSelector, addressTwoSelector);

        // TODO:  Fix Filter
        SelectionFilter.Builder filterBuilder = SelectionFilter.builder();
        filterBuilder.address2(new AddressLineTwo("27", UnitType.UNIT));

        SelectionFilter filter = filterBuilder.build();
        StreetAddress streetAddress1 = streetAddressGenerator.generate(filter);
        LOGGER.debug("streetAddress1={}", streetAddress1.toString());

        Assertions.assertThat(streetAddress1.addressLineOne()).isNotNull();
        Assertions.assertThat(streetAddress1.addressLineTwo()).isNotNull();
        Assertions.assertThat(streetAddress1.addressLineTwo().address2().toUpperCase()).contains("UNIT").contains("27");
    }
}
