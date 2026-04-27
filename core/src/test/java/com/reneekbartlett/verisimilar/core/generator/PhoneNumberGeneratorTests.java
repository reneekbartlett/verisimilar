package com.reneekbartlett.verisimilar.core.generator;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.PhoneNumberDatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.model.PhoneNumber;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.AreaCodeSelectionEngine;

public class PhoneNumberGeneratorTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneNumberGeneratorTests.class);

    //@Test
    public void GeneratePhoneNumber() {

        PhoneNumberDatasetResolverRegistry registry = TestUtils.getPhoneNumberDatasetResolverRegistry();
        AreaCodeSelectionEngine areaCodeSelector = new AreaCodeSelectionEngine(registry, TestUtils.UNIFORM_RANDOM);

        PhoneNumberGenerator phoneNumberGenerator = new PhoneNumberGenerator(areaCodeSelector);

        PhoneNumber phoneNumber1 = phoneNumberGenerator.generate();
        LOGGER.debug(phoneNumber1.toString());

        // 2nd attempt should be cached
        PhoneNumber phoneNumber2 = phoneNumberGenerator.generate();
        LOGGER.debug(phoneNumber2.toString());

        // 3rd attempt should be cached
        PhoneNumber phoneNumber3 = phoneNumberGenerator.generate();
        LOGGER.debug(phoneNumber3.toString());
    }

    @Test
    public void GeneratePhoneNumber_StateCriteria_MA() {
        PhoneNumberDatasetResolverRegistry registry = TestUtils.getPhoneNumberDatasetResolverRegistry();
        AreaCodeSelectionEngine areaCodeSelector = new AreaCodeSelectionEngine(registry, TestUtils.UNIFORM_RANDOM);
        PhoneNumberGenerator phoneNumberGenerator = new PhoneNumberGenerator(areaCodeSelector);

        Set<USState> states = Set.of(USState.MA);
        DatasetResolutionContext ctx = DatasetResolutionContext.builder().states(states).build();
        SelectionFilter criteria = SelectionFilter.builder().states(states).build();

        PhoneNumber phoneNumber1 = phoneNumberGenerator.generate(ctx, criteria);
        LOGGER.debug(phoneNumber1.toString());

        String areaCodesStrForMA = "339|351|413|508|617|774|781|857|978";
        List<String> areaCodesForMA = Arrays.asList(areaCodesStrForMA.split("\\|"));
        String areaCode1 = phoneNumber1.areaCode();
        Assertions.assertTrue(areaCodesForMA.contains(areaCode1));
    }

    //AK,907
    @Test
    public void GeneratePhoneNumber_StateCriteria_AK() {
        PhoneNumberDatasetResolverRegistry registry = TestUtils.getPhoneNumberDatasetResolverRegistry();
        AreaCodeSelectionEngine areaCodeSelector = new AreaCodeSelectionEngine(registry, TestUtils.UNIFORM_RANDOM);
        PhoneNumberGenerator phoneNumberGenerator = new PhoneNumberGenerator(areaCodeSelector);

        Set<USState> states = Set.of(USState.AK);
        DatasetResolutionContext ctx = DatasetResolutionContext.builder().states(states).build();
        SelectionFilter criteria = SelectionFilter.builder().states(states).build();

        PhoneNumber phoneNumber1 = phoneNumberGenerator.generate(ctx, criteria);
        LOGGER.debug(phoneNumber1.toString());

        String areaCodesStrForAK = "907";
        List<String> areaCodesForAK = Arrays.asList(areaCodesStrForAK.split("\\|"));
        String areaCode1 = phoneNumber1.areaCode();
        Assertions.assertTrue(areaCodesForAK.contains(areaCode1));
    }
}
