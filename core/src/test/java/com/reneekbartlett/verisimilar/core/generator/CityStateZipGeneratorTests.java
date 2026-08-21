package com.reneekbartlett.verisimilar.core.generator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.model.CityStateZip;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.engine.CityStateZipSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.PostalAddressSelectionEngineRegistry;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

@DisabledIf(value = "com.reneekbartlett.verisimilar.core.TestUtils#isCoreTestingDisabled")
public class CityStateZipGeneratorTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(CityStateZipGeneratorTests.class);

    @Test
    public void GenerateCityStateZip_Random() {
        PostalAddressSelectionEngineRegistry registry = TestUtils.getPostalAddressSelectionEngineRegistry();
        CityStateZipGenerator cityStateZipGenerator = new CityStateZipGenerator(registry.cityStateZip());

        CityStateZip cityStateZip = cityStateZipGenerator.generate();
        LOGGER.debug("cityStateZip={}", cityStateZip.toString());

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
        LOGGER.debug("cityStateZip={}", cityStateZip.toString());

        assertThat(cityStateZip).isNotNull();
        assertThat(cityStateZip.state()).isEqualTo(USState.MA.getLabel());
        Assertions.assertTrue(states.contains(USState.valueOf(cityStateZip.state())));
    }

    @Test
    public void GenerateCityStateZip_StateZipCriteria_ShrewsburyMA() {
        //PostalAddressSelectionEngineRegistry registry = TestUtils.getPostalAddressSelectionEngineRegistry();
        DatasetResolverRegistry resolvers = TestUtils.getDatasetResolverRegistry();
        CityStateZipSelectionEngine cityStateZipSelector = new CityStateZipSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM);
        CityStateZipGenerator cityStateZipGenerator = new CityStateZipGenerator(cityStateZipSelector);

        USState ma = USState.MA;
        EnumSet<USState> states = EnumSet.of(USState.MA);
        Set<String> zipCodes = Set.of("01545");

        //
        // SelectionFilter
        //
        SelectionFilter filter = SelectionFilter.builder()
                .states(states)
                .zipCodes(zipCodes)
                .build();

        //
        // CityStateZip
        //
        CityStateZip cityStateZip = cityStateZipGenerator.generate(filter);
        LOGGER.debug("cityStateZip={}", cityStateZip.toString());
        assertThat(cityStateZip).isNotNull();
        assertThat(cityStateZip.state()).isIn(ma.getLabel());
        assertThat(cityStateZip.zip()).isIn(zipCodes);
    }

    @Test
    public void GenerateCityStateZip_StateZipsCriteria_ShrewsburyMA() {
        //PostalAddressSelectionEngineRegistry registry = TestUtils.getPostalAddressSelectionEngineRegistry();
        DatasetResolverRegistry resolvers = TestUtils.getDatasetResolverRegistry();
        CityStateZipSelectionEngine cityStateZipSelector = new CityStateZipSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM);
        CityStateZipGenerator cityStateZipGenerator = new CityStateZipGenerator(cityStateZipSelector);

        USState ma = USState.MA;
        EnumSet<USState> states = EnumSet.of(USState.MA);
        Set<String> zipCodes = Set.of("01545", "01546");

        //
        // SelectionFilter
        //
        SelectionFilter filter = SelectionFilter.builder()
                .states(states)
                .zipCodes(zipCodes)
                .build();

        //
        // CityStateZip
        //
        CityStateZip cityStateZip = cityStateZipGenerator.generate(filter);
        LOGGER.debug("cityStateZip={}", cityStateZip.toString());
        assertThat(cityStateZip).isNotNull();
        assertThat(cityStateZip.state()).isIn(ma.getLabel());
        assertThat(cityStateZip.zip()).isIn(zipCodes);
    }

    @Test
    public void GenerateCityStateZip_CityZipCriteria_ShrewsburyMA() {
        //PostalAddressSelectionEngineRegistry registry = TestUtils.getPostalAddressSelectionEngineRegistry();
        DatasetResolverRegistry resolvers = TestUtils.getDatasetResolverRegistry();
        CityStateZipSelectionEngine cityStateZipSelector = new CityStateZipSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM);
        CityStateZipGenerator cityStateZipGenerator = new CityStateZipGenerator(cityStateZipSelector);

        Set<String> zipCodes = Set.of("01545");

        //
        // SelectionFilter
        //
        SelectionFilter filter = SelectionFilter.builder()
                .city("SHREWSBURY")
                .zipCodes(zipCodes)
                .build();

        //
        // CityStateZip
        //
        CityStateZip cityStateZip = cityStateZipGenerator.generate(filter);
        LOGGER.debug("cityStateZip={}", cityStateZip.toString());
        assertThat(cityStateZip).isNotNull();
        //assertThat(cityStateZip.state()).isIn(ma.getLabel());
        assertThat(cityStateZip.city()).isEqualTo("SHREWSBURY");
        assertThat(cityStateZip.zip()).isIn(zipCodes);
    }

    @Test
    public void GenerateCityStateZip_CityZipsCriteria_Newton_MultiZips() {
        //PostalAddressSelectionEngineRegistry registry = TestUtils.getPostalAddressSelectionEngineRegistry();
        DatasetResolverRegistry resolvers = TestUtils.getDatasetResolverRegistry();
        CityStateZipSelectionEngine cityStateZipSelector = new CityStateZipSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM);
        CityStateZipGenerator cityStateZipGenerator = new CityStateZipGenerator(cityStateZipSelector);

        Set<String> zipCodes = Set.of("02458", "02460");

        //
        // SelectionFilter
        //
        SelectionFilter filter = SelectionFilter.builder()
                .city("NEWTON ")
                .zipCodes(zipCodes)
                .build();

        //
        // CityStateZip
        //
        CityStateZip cityStateZip = cityStateZipGenerator.generate(filter);
        LOGGER.debug("cityStateZip={}", cityStateZip.toString());
        assertThat(cityStateZip).isNotNull();
        //assertThat(cityStateZip.state()).isIn(ma.getLabel());
        assertThat(cityStateZip.city()).isEqualTo("NEWTON");
        assertThat(cityStateZip.zip()).isIn(zipCodes);
    }
}
