package com.reneekbartlett.verisimilar.core.generator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.PhoneNumberDatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.model.PhoneNumber;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.engine.AreaCodeSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

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

    //@Test
    public void GeneratePhoneNumber_StateCriteria_MA() {
        PhoneNumberDatasetResolverRegistry registry = TestUtils.getPhoneNumberDatasetResolverRegistry();
        AreaCodeSelectionEngine areaCodeSelector = new AreaCodeSelectionEngine(registry, TestUtils.UNIFORM_RANDOM);
        PhoneNumberGenerator phoneNumberGenerator = new PhoneNumberGenerator(areaCodeSelector);

        EnumSet<USState> states = EnumSet.of(USState.MA);
        DatasetResolutionContext ctx = DatasetResolutionContext.builder().states(states).build();
        SelectionFilter criteria = SelectionFilter.builder().states(states).build();

        PhoneNumber phoneNumber1 = phoneNumberGenerator.generate(ctx, criteria);
        LOGGER.debug(phoneNumber1.toString());

        String areaCodesStrForMA = "339|351|413|508|617|774|781|857|978";
        List<String> areaCodesForMA = Arrays.asList(areaCodesStrForMA.split("\\|"));

        assertThat(phoneNumber1.areaCode()).isIn(areaCodesForMA);
    }

    //AK,907
    //@Test
    public void GeneratePhoneNumber_StateCriteria_AK() {
        PhoneNumberDatasetResolverRegistry registry = TestUtils.getPhoneNumberDatasetResolverRegistry();
        AreaCodeSelectionEngine areaCodeSelector = new AreaCodeSelectionEngine(registry, TestUtils.UNIFORM_RANDOM);
        PhoneNumberGenerator phoneNumberGenerator = new PhoneNumberGenerator(areaCodeSelector);

        EnumSet<USState> states = EnumSet.of(USState.AK);
        DatasetResolutionContext ctx = DatasetResolutionContext.builder().states(states).build();

        SelectionFilter criteria = SelectionFilter.builder()
                .states(states)
                .build();

        PhoneNumber phoneNumber1 = phoneNumberGenerator.generate(ctx, criteria);
        LOGGER.debug(phoneNumber1.toString());

        String areaCodesStrForAK = "907";
        List<String> areaCodesForAK = Arrays.asList(areaCodesStrForAK.split("\\|"));

        assertThat(phoneNumber1.areaCode()).isEqualTo("907").isIn(areaCodesForAK);
    }

    @Test
    public void GeneratePhoneNumber_AreaCodeCriteria_917() {
        PhoneNumberDatasetResolverRegistry registry = TestUtils.getPhoneNumberDatasetResolverRegistry();
        AreaCodeSelectionEngine areaCodeSelector = new AreaCodeSelectionEngine(registry, TestUtils.UNIFORM_RANDOM);
        PhoneNumberGenerator phoneNumberGenerator = new PhoneNumberGenerator(areaCodeSelector);

        SelectionFilter criteria = SelectionFilter.builder()
                .areaCode("917")
                .build();

        PhoneNumber phoneNumber1 = phoneNumberGenerator.generate(criteria);
        LOGGER.debug(phoneNumber1.toString());

        assertThat(phoneNumber1.toString()).matches("^\\d{3}-\\d{3}-\\d{4}$");

        assertThat(phoneNumber1.areaCode()).isEqualTo("917").containsOnlyDigits();

        assertThat(phoneNumber1.exchangeCode()).isNotNull().containsOnlyDigits();

        assertThat(phoneNumber1.lineNumber()).isNotNull().containsOnlyDigits();

    }
}
