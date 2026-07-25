package com.reneekbartlett.verisimilar.core.generator;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.text.StringSubstitutor;

import com.reneekbartlett.verisimilar.core.model.AddressCategory;
import com.reneekbartlett.verisimilar.core.model.StreetAddress;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.UnitType;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.util.RandomUtils;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.engine.AddressTwoSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetSuffixSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;

public class StreetAddressGenerator extends AbstractValueGenerator<StreetAddress>{

    private static final String[] ADDRESS2_UNIT_XTRA = { "A", "B", "C", "D", "E", "F", "N", "S", "E", "W" };

    // TODO
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

    public StreetAddressGenerator(DatasetSelectionEngineRegistry selectors) {
        this(selectors.streetName(), selectors.streetSuffix(), selectors.addressTwo());
    }

    @Override
    protected StreetAddress generateValue(DatasetResolutionContext ctx, SelectionFilter filter) {
        if(filter == null) filter = SelectionFilter.empty();
        return generateStreetAddress(ctx, filter);
    }

    @Override
    protected Class<StreetAddress> valueType() {
        return StreetAddress.class;
    }

    private StreetAddress generateStreetAddress(DatasetResolutionContext ctx, SelectionFilter filter) {
        AddressCategory addressCategory = getAddressCategory(filter);
        if(addressCategory == AddressCategory.PO_BOX) {
            return new StreetAddress(getPostOfficeBox(filter), null, addressCategory);
        }

        String address1 = getAddressLineOne(filter, addressCategory);
        String address2 = getAddressLineTwo(filter, addressCategory);
        return new StreetAddress(address1, address2, addressCategory);
    }

    private AddressCategory getAddressCategory(SelectionFilter filter) {
        String addressCategoryFilter = filter.equalToMap().get(TemplateField.ADDRESS_CATEGORY);
        if (addressCategoryFilter != null) {
            return AddressCategory.fromLabel(addressCategoryFilter);
        }
        return new WeightedSelectorImpl<>(AddressCategory.defaultMap(), TemplateField.ADDRESS_CATEGORY).select();
    }

    /***
     * AddressLineOne
     * @param filter
     * @param addressCategory
     * @return
     */
    private String getAddressLineOne(SelectionFilter filter, AddressCategory addressCategory) {
        String streetId = getStreetId(filter, addressCategory);
        String streetName = getStreetName(filter, addressCategory);
        String streetSuffix = getStreetSuffix(filter, addressCategory);
        return new StringBuilder(40)
                .append(streetId).append(" ")
                .append(streetName).append(" ")
                .append(streetSuffix).toString().toUpperCase();
    }

    // TODO: RANDOMIZE 10's, 100's, with weight towards lower number or AddressCategory
    private String getStreetId(SelectionFilter filter, AddressCategory addressCategory) {
        int randHouseNum = RandomUtils.getSkewedRandom(1, 10000, 2.0);
        return String.valueOf(randHouseNum);
    }

    private String getStreetName(SelectionFilter filter, AddressCategory addressCategory) {
        String streetNameFilter = filter.equalToMap().get(TemplateField.STREET_NAME);
        if(streetNameFilter != null) {
            return streetNameFilter;
        }
        return streetNameSelector.select(filter);
    }

    private String getStreetSuffix(SelectionFilter filter, AddressCategory addressCategory) {
        String streetSuffixFilter = filter.equalToMap().get(TemplateField.STREET_SUFFIX);
        if(streetSuffixFilter != null) {
            return streetSuffixFilter;
        }
        return streetSuffixSelector.select(filter);
    }

    /***
     * AddressLineTwo
     * @param filter
     * @param addressCategory
     * @return
     */
    private String getAddressLineTwo(SelectionFilter filter, AddressCategory addressCategory) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();

        String unitType = getUnitType(filter, addressCategory);
        int unitNumber = getUnitNumber(filter, unitType);
        String unitXtra = RandomUtils.getRandom(ADDRESS2_UNIT_XTRA, rand);

        Map<String, Object> params = Map.of(
                "UNIT", unitNumber, 
                "UNIT_TYPE", unitType, 
                "UNIT_XTRA", unitXtra
        );

        // Choose a random template, which may or may not include all param values.
        String template = RandomUtils.getRandom(ADDRESS2_TEMPLATES, rand);

        return StringSubstitutor.replace(template, params, "${", "}");
    }

    //UnitType
    private String getUnitType(SelectionFilter filter, AddressCategory addressCategory) {
        String unitTypeFilter = filter.equalToMap().get(TemplateField.UNIT_TYPE);
        if(unitTypeFilter != null) {
            return unitTypeFilter;
        }

        // TODO:  Replace addressTwoSelector?
        Map<UnitType, Double> weightedMap = UnitType.defaultMap();
        UnitType randomUnitType = new WeightedSelectorImpl<>(weightedMap, TemplateField.UNIT_TYPE).select();
        LOGGER.debug("UnitType (enum)=", randomUnitType);

        return addressTwoSelector.select(filter);
    }

    // TODO: Choose min/max based on UnitType
    private int getUnitNumber(SelectionFilter filter, String unitType) {
        String unitNumberFilter = filter.equalToMap().get(TemplateField.UNIT_NUMBER);
        if(unitNumberFilter != null) {
            return NumberUtils.toInt(unitNumberFilter, 100);
        }
        return RandomUtils.getSkewedRandom(1, 1000, 1.0);
    }

    /***
     * AddressCategory.PO_BOX
     * @param filter
     * @return
     */
    private String getPostOfficeBox(SelectionFilter filter) {
        int randBoxNum = RandomUtils.getSkewedRandom(1, 10000, 3.0);
        return new StringBuilder().append("PO BOX ").append(String.valueOf(randBoxNum)).toString();
    }

}
