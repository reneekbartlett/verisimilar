package com.reneekbartlett.verisimilar.core.selector;

import org.junit.jupiter.api.Test;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.UnitType;

import org.junit.jupiter.api.BeforeEach;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

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
        assertTrue(countApartment > countPenthouse, () -> "APARTMENT should be selected more often than PENTHOUSE. [" + countsStr + "]");
        //assertTrue(countOther > 0, "countOther should be GT 0");
    }
}