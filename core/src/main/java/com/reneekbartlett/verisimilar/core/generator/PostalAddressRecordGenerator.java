package com.reneekbartlett.verisimilar.core.generator;

import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.AddressTwoSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.CityStateZipSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetSuffixSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;

import java.util.List;

import com.reneekbartlett.verisimilar.core.model.CityStateZip;
import com.reneekbartlett.verisimilar.core.model.PostalAddress;
import com.reneekbartlett.verisimilar.core.model.StreetAddress;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.USState;

public class PostalAddressRecordGenerator extends AbstractValueGenerator<PostalAddress>{

    private final StreetAddressGenerator streetAddressGenerator;
    private final CityStateZipGenerator cityStateZipGenerator;
    private final List<TemplateField> filterFields;

    public PostalAddressRecordGenerator(
            StreetNameSelectionEngine streetNameSelector,
            StreetSuffixSelectionEngine streetSuffixSelector,
            AddressTwoSelectionEngine addressTwoSelector,
            CityStateZipSelectionEngine cityStateZipSelector) {
        this.cityStateZipGenerator = new CityStateZipGenerator(cityStateZipSelector);
        this.streetAddressGenerator = new StreetAddressGenerator(streetNameSelector, streetSuffixSelector, addressTwoSelector);
        this.filterFields = List.of(TemplateField.CITY, TemplateField.STATE, TemplateField.ZIP_CODE,
                TemplateField.STREET_ID, TemplateField.STREET_NAME, TemplateField.STREET_SUFFIX,
                TemplateField.ADDRESS_CATEGORY, 
                TemplateField.UNIT_TYPE, TemplateField.UNIT_NUMBER, TemplateField.UNIT_XTRA
        );
    }

    public PostalAddressRecordGenerator(DatasetSelectionEngineRegistry selectors) {
        this(selectors.streetName(), selectors.streetSuffix(), selectors.addressTwo(), selectors.cityStateZip());
    }

    @Override
    protected PostalAddress generateValue(DatasetResolutionContext ctx, SelectionFilter filter) {
        return generatePostalAddress(ctx, filter);
    }

    private PostalAddress generatePostalAddress(DatasetResolutionContext ctx, SelectionFilter filter) {
        // Use the filter for the CityStateZip
        CityStateZip cityStateZip = generateCityStateZip(ctx, filter);

        // Create a new filter
        SelectionFilter streetAddressFilter = SelectionFilter.toBuilder(filter)
                .state(USState.fromAbbreviation(cityStateZip.state()))
                .city(cityStateZip.city())
                .build();
        StreetAddress streetAddress = generateStreetAddress(ctx, streetAddressFilter);

        LOGGER.debug("generatePostalAddress - cityStateZip={}", cityStateZip);
        
        return new PostalAddress(streetAddress.address1(), streetAddress.address2(), cityStateZip);
    }

    private CityStateZip generateCityStateZip(DatasetResolutionContext ctx, SelectionFilter filter) {
        return cityStateZipGenerator.generate(ctx, filter);
    }

    private StreetAddress generateStreetAddress(DatasetResolutionContext ctx, SelectionFilter filter) {
        return streetAddressGenerator.generate(ctx, filter);
    }

    @Override
    protected Class<PostalAddress> valueType() {
        return PostalAddress.class;
    }

    @Override
    public List<TemplateField> filterFields(){
        return this.filterFields;
    }
}
