package com.reneekbartlett.verisimilar.core.selector.engine.registry;

import com.reneekbartlett.verisimilar.core.selector.engine.AddressTwoSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.CityStateZipSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetSuffixSelectionEngine;

public class PostalAddressSelectionEngineRegistry extends DatasetSelectionEngineRegistry{
    public PostalAddressSelectionEngineRegistry(
            StreetNameSelectionEngine streetNameSelector,
            StreetSuffixSelectionEngine streetSuffixSelector,
            AddressTwoSelectionEngine addressTwoSelector,
            CityStateZipSelectionEngine cityStateZipSelector
    ) {
        super(
                null, null, null, null, // first/middle/last/nickname
                streetNameSelector, streetSuffixSelector, addressTwoSelector, 
                cityStateZipSelector,
                null, // areaCode
                null, // username
                null, // domain
                null // keyword
        );
    }
}
