package com.reneekbartlett.verisimilar.core.generator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.text.StringSubstitutor;

import com.reneekbartlett.verisimilar.core.model.AddressCategory;
import com.reneekbartlett.verisimilar.core.model.AddressLineOne;
import com.reneekbartlett.verisimilar.core.model.AddressLineTwo;
import com.reneekbartlett.verisimilar.core.model.StreetAddress;
import com.reneekbartlett.verisimilar.core.model.StreetSuffix;
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

    // TODO:  Make enum?
    private static final String[] ADDRESS2_UNIT_XTRA = { "A", "B", "C", "D", "E", "F", "N", "S", "E", "W" };

    private static final String ADDRESS2_TEMPLATE_DEFAULT = "${UNIT_TYPE} ${UNIT}"; // APARTMENT 1

    private static final String[] ADDRESS2_TEMPLATES = {
            ADDRESS2_TEMPLATE_DEFAULT, // APARTMENT 1
            "${UNIT_TYPE} ${UNIT}${UNIT_XTRA}", // APARTMENT 1B
            "${UNIT_TYPE} ${UNIT_XTRA}", // APARTMENT S
            "${UNIT_TYPE} ${UNIT_XTRA}${UNIT}" // B1
    };

    private final StreetNameSelectionEngine streetNameSelector;
    private final StreetSuffixSelectionEngine streetSuffixSelector;
    private final AddressTwoSelectionEngine addressTwoSelector;
    private final List<TemplateField> filterFields;

    public StreetAddressGenerator(
            StreetNameSelectionEngine streetNameSelector, 
            StreetSuffixSelectionEngine streetSuffixSelector,
            AddressTwoSelectionEngine addressTwoSelector
    ) {
        this.streetNameSelector = streetNameSelector;
        this.streetSuffixSelector = streetSuffixSelector;
        this.addressTwoSelector = addressTwoSelector;
        this.filterFields = List.of(
                TemplateField.STREET_ID, TemplateField.STREET_NAME, TemplateField.STREET_SUFFIX,
                TemplateField.ADDRESS_CATEGORY, 
                TemplateField.UNIT_TYPE, TemplateField.UNIT_NUMBER, TemplateField.UNIT_XTRA
        );
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

    @Override
    public List<TemplateField> filterFields() {
        return this.filterFields;
    }

    private StreetAddress generateStreetAddress(DatasetResolutionContext ctx, SelectionFilter filter) {

        AddressCategory addressCategory = filter.addressCategory().orElseGet(() -> generateAddressCategory(filter));

        if(addressCategory == AddressCategory.PO_BOX) {
            return new StreetAddress(getPostOfficeBox(filter), null, addressCategory);
        }

        AddressLineOne address1 = getAddressLineOne(filter, addressCategory);
        AddressLineTwo address2 = getAddressLineTwo(filter, addressCategory);
        return new StreetAddress(address1, address2, addressCategory);
    }

    private AddressCategory generateAddressCategory(SelectionFilter filter) {
        return new WeightedSelectorImpl<>(AddressCategory.defaultMap(), TemplateField.ADDRESS_CATEGORY).select();
    }

    /***
     * AddressLineOne
     * @param filter
     * @param addressCategory
     * @return
     */
    private AddressLineOne getAddressLineOne(SelectionFilter filter, AddressCategory addressCategory) {
        String streetId = filter.streetId().orElseGet(() -> generateStreetId(filter, addressCategory));
        String streetName = filter.streetName().orElseGet(() -> generateStreetName(filter, addressCategory));
        String streetSuffix = filter.streetSuffix().orElseGet(() -> generateStreetSuffix(filter, addressCategory));
        String address1 = new StringBuilder(50).append(streetId).append(" ").append(streetName).append(" ")
                .append(streetSuffix).toString().toUpperCase();
        return new AddressLineOne(address1, streetId, streetName, StreetSuffix.fromLabel(streetSuffix), addressCategory);
    }

    // TODO: RANDOMIZE 10's, 100's, with weight towards lower number or AddressCategory
    private String generateStreetId(SelectionFilter filter, AddressCategory addressCategory) {
        int randHouseNum = RandomUtils.getSkewedRandom(1, 10000, 2.0);
        return String.valueOf(randHouseNum);
    }

    private String generateStreetName(SelectionFilter filter, AddressCategory addressCategory) {
        return streetNameSelector.select(filter);
    }

    private String generateStreetSuffix(SelectionFilter filter, AddressCategory addressCategory) {
        return streetSuffixSelector.select(filter);
    }

    /***
     * AddressLineTwo
     * @param filter
     * @param addressCategory
     * @return
     */
    private AddressLineTwo getAddressLineTwo(SelectionFilter filter, AddressCategory addressCategory) {

        UnitType unitType = filter.unitType().orElseGet(() -> generateUnitType(filter));

        // If both UnitType and UnitNumber are specified in the Filter, use the default template containing both fields
        String template;
        Optional<String> unitXtra = Optional.empty();
        if(filter.unitType().isPresent() && filter.unitNumber().isPresent()) {
            template = ADDRESS2_TEMPLATE_DEFAULT;
        } else {
            // Choose a random template, which may or may not include all param values.
            template = RandomUtils.getRandom(ADDRESS2_TEMPLATES, ThreadLocalRandom.current());
            unitXtra = Optional.of(generateUnitExtra(filter, unitType));
        }

        String unitNumberStr = filter.unitNumber().orElseGet(() -> String.valueOf(generateUnitNumber(filter, unitType)));
        Map<String, Object> params = new HashMap<>();
        params.put("UNIT", unitNumberStr);
        params.put("UNIT_TYPE", unitType);
        params.put("UNIT_XTRA", unitXtra.orElse(""));

        String address2 = StringSubstitutor.replace(template, params, "${", "}");

        //AddressLineTwo (String address2, String unitNumber, String unitXtra, UnitType unitType, AddressCategory addressCategory)
        return new AddressLineTwo(address2, unitNumberStr, unitXtra.orElse(""), unitType, addressCategory);
    }

    private UnitType generateUnitType(SelectionFilter filter) {
        Map<UnitType, Double> weightedMap = UnitType.defaultMap();
        UnitType randomUnitType = new WeightedSelectorImpl<>(weightedMap, TemplateField.UNIT_TYPE).select();
        LOGGER.debug("UnitType (enum)={}", randomUnitType);
        return randomUnitType;
    }

    private int generateUnitNumber(SelectionFilter filter, UnitType unitType) {
        // TODO:  Adjust min/max based on UnitType
        return RandomUtils.getSkewedRandom(1, 1000, 1.0);
    }

    private String generateUnitExtra(SelectionFilter filter, UnitType unitType) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        return RandomUtils.getRandom(ADDRESS2_UNIT_XTRA, rand);
    }

    /***
     * AddressCategory.PO_BOX
     * @param filter
     * @return
     */
    private AddressLineOne getPostOfficeBox(SelectionFilter filter) {
        String randBoxNum = String.valueOf(RandomUtils.getSkewedRandom(1, 10000, 3.0));
        String address1 = new StringBuilder().append("PO BOX ").append(String.valueOf(randBoxNum)).toString();
        //(String streetId, String streetName, StreetSuffix streetSuffix, AddressCategory addressCategory)
        return new AddressLineOne(address1, randBoxNum, null, StreetSuffix.NONE, AddressCategory.PO_BOX);
    }

}
