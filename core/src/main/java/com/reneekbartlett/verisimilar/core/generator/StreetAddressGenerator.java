package com.reneekbartlett.verisimilar.core.generator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.text.StringSubstitutor;

import com.reneekbartlett.verisimilar.core.generator.api.AbstractValueGenerator;
import com.reneekbartlett.verisimilar.core.model.AddressCategory;
import com.reneekbartlett.verisimilar.core.model.StreetAddress;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.engine.AddressTwoSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetSuffixSelectionEngine;

public class StreetAddressGenerator extends AbstractValueGenerator<StreetAddress>{

    private static final String[] ADDRESS2_UNIT_XTRA = { "A", "B", "C", "D", "E", "F", "N", "S", "E", "W" };

    private static final String[] ADDRESS2_TEMPLATES = {
            "${UNIT_TYPE} ${UNIT}", // APARTMENT 1
            "${UNIT_TYPE} ${UNIT}${UNIT_XTRA}", // APARTMENT 1B
            "${UNIT_TYPE} ${UNIT_XTRA}", // APARTMENT S
            "${UNIT_TYPE} ${UNIT_XTRA}${UNIT}" // B1
    };

    private final StreetNameSelectionEngine streetNameSelector;
    private final StreetSuffixSelectionEngine streetSuffixSelector;
    private final AddressTwoSelectionEngine addressTwoSelector;

    public StreetAddressGenerator(
            StreetNameSelectionEngine streetNameSelector, 
            StreetSuffixSelectionEngine streetSuffixSelector,
            AddressTwoSelectionEngine addressTwoSelector
    ) {
        this.streetNameSelector = streetNameSelector;
        this.streetSuffixSelector = streetSuffixSelector;
        this.addressTwoSelector = addressTwoSelector;
    }

    @Override
    protected StreetAddress generateValue(DatasetResolutionContext ctx, SelectionFilter filter) {
        return generateStreetAddress(ctx, filter);
    }

    @Override
    protected Class<StreetAddress> valueType() {
        return StreetAddress.class;
    }

    private StreetAddress generateStreetAddress(DatasetResolutionContext ctx, SelectionFilter filter) {
        if(filter == null) {
            filter = SelectionFilter.empty();
        }

        AddressCategory addressCategory = getAddressCategory(filter);

        if(addressCategory == AddressCategory.PO_BOX) {
            String address1 = getPostOfficeBox(filter);
            return new StreetAddress(address1, null, addressCategory.getLabel());
        }

        String address1 = getAddress1(filter);

        String address2 = getAddress2(filter);

        return new StreetAddress(address1, address2, addressCategory.getLabel());
    }

    private AddressCategory getAddressCategory(SelectionFilter filter) {
        if(filter.equalToMap().containsKey(TemplateField.ADDRESS_CATEGORY)) {
            return AddressCategory.fromLabel(filter.equalToMap().get(TemplateField.ADDRESS_CATEGORY));
        }
        Map<AddressCategory, Double> weightedMap = AddressCategory.defaultMap();
        AddressCategory randomCategory = new WeightedSelectorImpl<>(weightedMap).select();
        LOGGER.debug("randomCategory:{}", randomCategory);
        return randomCategory;
    }

    private String getAddress1(SelectionFilter filter) {
        String streetId = getStreetId(filter);

        String streetName = getStreetName(filter);

        String streetSuffix = getStreetSuffix(filter);

        return new StringBuilder(40)
                .append(streetId).append(" ")
                .append(streetName).append(" ")
                .append(streetSuffix).toString().toUpperCase();
    }

    private String getStreetId(SelectionFilter filter) {
        // TODO: RANDOMIZE 10's, 100's, with weight towards lower number
        //int houseNumber = rand.nextInt(1, 10000);
        // The power value determines the skew.
        // Value >1 skews towards the minimum (lower numbers), <1 skews towards the maximum (higher numbers)
        int randHouseNum = getSkewedRandom(1, 10000, 3.0);
        return String.valueOf(randHouseNum);
    }

    private String getStreetName(SelectionFilter filter) {
        // TODO:  Double check handling of empty/null/errors
        if(filter.equalToMap().containsKey(TemplateField.STREET_NAME)) {
            return filter.equalToMap().get(TemplateField.STREET_NAME);
        }
        return streetNameSelector.select(filter);
    }

    private String getStreetSuffix(SelectionFilter filter) {
        if(filter.equalToMap().containsKey(TemplateField.STREET_SUFFIX)) {
            return filter.equalToMap().get(TemplateField.STREET_SUFFIX);
        }
        return streetSuffixSelector.select(filter);
    }

    private String getAddress2(SelectionFilter filter) {
        String unitType = getUnitType(filter);

        // TODO:  break out unit number by tens, hundreds, etc. Prob. influenced by unit type, city
        int unitNumber = getUnitNumber();

        ThreadLocalRandom rand = ThreadLocalRandom.current();
        String unitXtra = ADDRESS2_UNIT_XTRA[rand.nextInt(ADDRESS2_UNIT_XTRA.length)];

        String randTemplate = ADDRESS2_TEMPLATES[rand.nextInt(ADDRESS2_TEMPLATES.length)];
        Map<String, Object> params = new HashMap<>();
        params.put("UNIT", unitNumber);
        params.put("UNIT_TYPE", unitType);
        params.put("UNIT_XTRA", unitXtra);

        return StringSubstitutor.replace(randTemplate, params, "${", "}").toUpperCase();
    }

    private String getUnitType(SelectionFilter filter) {
        if(filter.equalToMap().containsKey(TemplateField.UNIT_TYPE)) {
            return filter.equalToMap().get(TemplateField.UNIT_TYPE);
        }
        return addressTwoSelector.select(filter);
    }

    private int getUnitNumber() {
        return ThreadLocalRandom.current().nextInt(1, 1000);
    }

    private String getPostOfficeBox(SelectionFilter filter) {
        int randBoxNum = getSkewedRandom(1, 10000, 3.0);
        return new StringBuilder().append("PO BOX ").append(String.valueOf(randBoxNum)).toString();
    }

    private static int getSkewedRandom(int min, int max, double skewPower) {
        Random random = new Random();
        // Generate a uniformly random double between 0.0 (inclusive) and 1.0 (exclusive)
        double uniformRandom = random.nextDouble();

        // Skew the value using Math.pow()
        double skewedRandom = Math.pow(uniformRandom, skewPower);

        // Map the skewed value to the desired range [min, max]
        // (max - min) + min ensures the range is inclusive of max
        int range = max - min + 1;
        int result = (int) (skewedRandom * range) + min;

        // Ensure the result is within the specified range due to casting and bounds
        return Math.min(Math.max(result, min), max);
    }
}
