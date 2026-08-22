package com.reneekbartlett.verisimilar.core.selector;

import java.util.EnumSet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.UnitType;

public class WeightedEnumSelectorImplTests {

    private TemplateField field;

    @BeforeEach
    public void setup() {
        field = TemplateField.UNIT_TYPE; // or mock if needed
    }

    @Test
    public void testSelectReturnsWeightedItem() {
        EnumSet<UnitType> unitTypeSet = EnumSet.allOf(UnitType.class);
        WeightedEnumSelectorImpl<UnitType> selector = new WeightedEnumSelectorImpl<>(unitTypeSet, field);

        // Run many times to ensure distribution is respected
        int countApartment = 0;
        int countPenthouse = 0;
        int countOther = 0;

        for (int i = 0; i < 10_000; i++) {
            UnitType unitType = selector.select();
            if (unitType.equals(UnitType.APARTMENT)) countApartment++;
            else if (unitType.equals(UnitType.PENTHOUSE)) countPenthouse++;
            else countOther++;
        }

        final String countsStr = "PENTHOUSE=" + countPenthouse + ", APARTMENT=" + countApartment 
                + ", OTHER=" + countOther;
        Assertions.assertThat(countApartment).isGreaterThan(countPenthouse)
            .withFailMessage(() -> "APARTMENT should be selected more often than PENTHOUSE. [" + countsStr + "]");
        Assertions.assertThat(countOther).isGreaterThan(0);
    }
}