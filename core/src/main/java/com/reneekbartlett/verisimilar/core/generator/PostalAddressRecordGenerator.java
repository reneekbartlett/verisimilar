package com.reneekbartlett.verisimilar.core.generator;

import com.reneekbartlett.verisimilar.core.generator.api.AbstractValueGenerator;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.AddressTwoSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.CityStateZipSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetSuffixSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.PostalAddressSelectionEngineRegistry;
import com.reneekbartlett.verisimilar.core.model.CityStateZip;
import com.reneekbartlett.verisimilar.core.model.PostalAddress;
import com.reneekbartlett.verisimilar.core.model.StreetAddress;

public class PostalAddressRecordGenerator extends AbstractValueGenerator<PostalAddress>{

    //private static final String VALUE_DELIM = " ";
    //private static final String[] ADDRESS2_UNIT_XTRA = { "A", "B", "C", "D", "E", "F", "N", "S", "E", "W" };
    //private static final String[] ADDRESS2_TEMPLATES = {
    //        "${UNIT_TYPE} ${UNIT}", // APARTMENT 1
    //        "${UNIT_TYPE} ${UNIT}${UNIT_XTRA}", // APARTMENT 1B
    //        "${UNIT_TYPE} ${UNIT_XTRA}", // APARTMENT S
    //        "${UNIT_TYPE} ${UNIT_XTRA}${UNIT}" // B1
    //};

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

    public PostalAddressRecordGenerator(PostalAddressSelectionEngineRegistry postalSelectorRegistry) {
        this.cityStateZipGenerator = new CityStateZipGenerator(postalSelectorRegistry.cityStateZip());
        this.streetAddressGenerator = new StreetAddressGenerator(postalSelectorRegistry.streetName(), 
                postalSelectorRegistry.streetSuffix(), 
                postalSelectorRegistry.addressTwo());
    }

    @Override
    protected PostalAddress generateValue(DatasetResolutionContext ctx, SelectionFilter filter) {
        return generatePostalAddress(ctx, filter);
    }

    @Override
    protected Class<PostalAddress> valueType() {
        return PostalAddress.class;
    }

    private PostalAddress generatePostalAddress(DatasetResolutionContext ctx, SelectionFilter filter) {
        //LOGGER.debug("generatePostalAddress");

        // Use the filter for the CityStateZip
        CityStateZip cityStateZip = generateCityStateZip(ctx, filter);

        // Create a new filter
        SelectionFilter streetAddressFilter = filter.toBuilder().build();
        StreetAddress streetAddress = generateStreetAddress(ctx, streetAddressFilter);

        return new PostalAddress(streetAddress.address1(), streetAddress.address2(), cityStateZip);
    }

    private CityStateZip generateCityStateZip(DatasetResolutionContext ctx, SelectionFilter filter) {
        return cityStateZipGenerator.generate(ctx, filter);
    }

    private StreetAddress generateStreetAddress(DatasetResolutionContext ctx, SelectionFilter filter) {
        return streetAddressGenerator.generate(ctx, filter);
    }
}
