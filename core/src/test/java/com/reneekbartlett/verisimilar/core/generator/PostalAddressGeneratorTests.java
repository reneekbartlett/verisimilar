package com.reneekbartlett.verisimilar.core.generator;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.model.PostalAddress;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.PostalAddressSelectionEngineRegistry;

public class PostalAddressGeneratorTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostalAddressGeneratorTests.class);

    //@Test
    public void GeneratePostalAddress() {

        PostalAddressSelectionEngineRegistry registry = TestUtils.getPostalAddressSelectionEngineRegistry();
        PostalAddressRecordGenerator postalAddressGenerator = new PostalAddressRecordGenerator(registry);

        PostalAddress postalAddress = postalAddressGenerator.generate();
        LOGGER.debug(postalAddress.toString());

        Assertions.assertNotNull(postalAddress);
    }

    //@Test
    public void GeneratePostalAddress_StateCriteria_MA() {
        PostalAddressSelectionEngineRegistry registry = TestUtils.getPostalAddressSelectionEngineRegistry();
        PostalAddressRecordGenerator postalAddressGenerator = new PostalAddressRecordGenerator(registry);

        Set<USState> states = Set.of(USState.MA);
        DatasetResolutionContext ctx = DatasetResolutionContext.builder()
                .states(states)
                .build();
        SelectionFilter criteria = SelectionFilter.builder()
                .states(states).build();

        PostalAddress postalAddress = postalAddressGenerator.generate(ctx, criteria);
        LOGGER.debug(postalAddress.toString());

        Assertions.assertNotNull(postalAddress);
    }

    //@Test
    public void GeneratePostalAddress_StateCriteria_Multiple() {
        PostalAddressSelectionEngineRegistry registry = TestUtils.getPostalAddressSelectionEngineRegistry();
        PostalAddressRecordGenerator postalAddressGenerator = new PostalAddressRecordGenerator(registry);

        Set<USState> states = Set.of(USState.MA, USState.CT, USState.NH );
        DatasetResolutionContext ctx = DatasetResolutionContext.builder()
                .states(states)
                .build();
        SelectionFilter criteria = SelectionFilter.builder()
                .states(states).build();

        PostalAddress postalAddress1 = postalAddressGenerator.generate(ctx, criteria);
        LOGGER.debug(postalAddress1.toString());

        PostalAddress postalAddress2 = postalAddressGenerator.generate(ctx, criteria);
        LOGGER.debug(postalAddress2.toString());

        PostalAddress postalAddress3 = postalAddressGenerator.generate(ctx, criteria);
        LOGGER.debug(postalAddress3.toString());
    }

    //@Test
    public void GeneratePostalAddress_StateZipCriteria_ShrewsburyMA() {
        PostalAddressSelectionEngineRegistry registry = TestUtils.getPostalAddressSelectionEngineRegistry();
        PostalAddressRecordGenerator postalAddressGenerator = new PostalAddressRecordGenerator(registry);

        Set<USState> states = Set.of(USState.MA);
        //DatasetResolutionContext ctx = DatasetResolutionContext.builder()
        //        .states(states).zipCodes(Set.of("01545")).build();
        SelectionFilter criteria = SelectionFilter.builder()
                .states(states)
                .zipCodes(Set.of("01545"))
                .build();

        PostalAddress postalAddress = postalAddressGenerator.generate(criteria);

        LOGGER.debug(postalAddress.toString());

        Assertions.assertNotNull(postalAddress);
        Assertions.assertTrue(postalAddress.state().equalsIgnoreCase("MA"));
        Assertions.assertTrue(postalAddress.zip().equalsIgnoreCase("01545"));
    }

    @Test
    public void GeneratePostalAddress_StateZipCriteria_BadValue() {
        PostalAddressSelectionEngineRegistry registry = TestUtils.getPostalAddressSelectionEngineRegistry();
        PostalAddressRecordGenerator postalAddressGenerator = new PostalAddressRecordGenerator(registry);

        SelectionFilter filter = SelectionFilter.builder()
                .states(Set.of(USState.MA))
                //.zipCodes(Set.of("00000"))
                .build();

        PostalAddress postalAddress = postalAddressGenerator.generate(filter);

        LOGGER.debug(postalAddress.toString());

        Assertions.assertNotNull(postalAddress);
        Assertions.assertTrue(postalAddress.state().equalsIgnoreCase("MA"));
        //Assertions.assertTrue(postalAddress.zip().equalsIgnoreCase("01545"));
    }
}
