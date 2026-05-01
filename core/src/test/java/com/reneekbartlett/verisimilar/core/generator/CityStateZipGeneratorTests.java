package com.reneekbartlett.verisimilar.core.generator;

import java.util.EnumSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.model.CityStateZip;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.CityStateZipSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.PostalAddressSelectionEngineRegistry;

public class CityStateZipGeneratorTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(CityStateZipGeneratorTests.class);

    @Test
    public void GenerateCityStateZip_Random() {
        PostalAddressSelectionEngineRegistry registry = TestUtils.getPostalAddressSelectionEngineRegistry();
        CityStateZipGenerator cityStateZipGenerator = new CityStateZipGenerator(registry.cityStateZip());

        CityStateZip cityStateZip = cityStateZipGenerator.generate();
        LOGGER.debug(cityStateZip.toString());

        Assertions.assertNotNull(cityStateZip);
    }

    @Test
    public void GenerateCityStateZip_StateCriteria_MA() {
        PostalAddressSelectionEngineRegistry registry = TestUtils.getPostalAddressSelectionEngineRegistry();
        CityStateZipGenerator cityStateZipGenerator = new CityStateZipGenerator(registry.cityStateZip());

        EnumSet<USState> states = EnumSet.of(USState.MA);
        DatasetResolutionContext ctx = DatasetResolutionContext.builder()
                .states(states).build();
        SelectionFilter filter = SelectionFilter.builder()
                .states(states).build();

        CityStateZip cityStateZip = cityStateZipGenerator.generate(ctx, filter);
        LOGGER.debug(cityStateZip.toString());

        Assertions.assertNotNull(cityStateZip);
        Assertions.assertTrue(states.contains(USState.valueOf(cityStateZip.state())));
    }

    @Test
    public void GenerateCityStateZip_StateZipCriteria_ShrewsburyMA() {
        //PostalAddressSelectionEngineRegistry registry = TestUtils.getPostalAddressSelectionEngineRegistry();
        DatasetResolverRegistry resolvers = TestUtils.getDatasetResolverRegistry();
        CityStateZipSelectionEngine cityStateZipSelector = new CityStateZipSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM);
        CityStateZipGenerator cityStateZipGenerator = new CityStateZipGenerator(cityStateZipSelector);

        EnumSet<USState> states = EnumSet.of(USState.MA);
        Set<String> zipCodes = Set.of("01545");
        DatasetResolutionContext ctx = DatasetResolutionContext.builder()
                //.states(states)
                //.zipCodes(Set.of("01545"))
                .build();
        SelectionFilter filter = SelectionFilter.builder()
                .states(states)
                .zipCodes(Set.of("01545"))
                .build();

        CityStateZip cityStateZip = cityStateZipGenerator.generate(ctx, filter);

        LOGGER.debug(cityStateZip.toString());

        Assertions.assertNotNull(cityStateZip);
        Assertions.assertTrue(cityStateZip.state().equalsIgnoreCase("MA"));
        Assertions.assertTrue(zipCodes.contains(cityStateZip.zip()));
    }
}
