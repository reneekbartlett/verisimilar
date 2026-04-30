package com.reneekbartlett.verisimilar.core.generator;

import com.reneekbartlett.verisimilar.core.generator.api.AbstractValueGenerator;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.AddressTwoSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.CityStateZipSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetSuffixSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;
import com.reneekbartlett.verisimilar.core.model.CityStateZip;
import com.reneekbartlett.verisimilar.core.model.PostalAddress;
import com.reneekbartlett.verisimilar.core.model.StreetAddress;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.USState;

public class PostalAddressRecordGenerator extends AbstractValueGenerator<PostalAddress>{

    private final StreetAddressGenerator streetAddressGenerator;
    private final CityStateZipGenerator cityStateZipGenerator;

    public PostalAddressRecordGenerator(
            StreetNameSelectionEngine streetNameSelector,
            StreetSuffixSelectionEngine streetSuffixSelector,
            AddressTwoSelectionEngine addressTwoSelector,
            CityStateZipSelectionEngine cityStateZipSelector) {
        this.cityStateZipGenerator = new CityStateZipGenerator(cityStateZipSelector);
        this.streetAddressGenerator = new StreetAddressGenerator(streetNameSelector, streetSuffixSelector, addressTwoSelector);
    }

    public PostalAddressRecordGenerator(DatasetSelectionEngineRegistry selectors) {
        this.cityStateZipGenerator = new CityStateZipGenerator(selectors.cityStateZip());
        this.streetAddressGenerator = new StreetAddressGenerator(selectors.streetName(), selectors.streetSuffix(), selectors.addressTwo());
    }

    @Override
    protected PostalAddress generateValue(DatasetResolutionContext ctx, SelectionFilter filter) {
        return generatePostalAddress(ctx, filter);
    }

    private PostalAddress generatePostalAddress(DatasetResolutionContext ctx, SelectionFilter filter) {
        // Use the filter for the CityStateZip
        CityStateZip cityStateZip = generateCityStateZip(ctx, filter);

        // Create a new filter
        SelectionFilter streetAddressFilter = filter.toBuilder()
                .state(USState.fromAbbreviation(cityStateZip.state()))
                .equalTo(cityStateZip.city(), TemplateField.CITY)
                .equalTo(cityStateZip.state(), TemplateField.STATE)
                .build();
        StreetAddress streetAddress = generateStreetAddress(ctx, streetAddressFilter);

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
}
