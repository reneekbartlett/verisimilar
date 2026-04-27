package com.reneekbartlett.verisimilar.core.selector.engine.registry;

import com.reneekbartlett.verisimilar.core.selector.engine.AreaCodeSelectionEngine;

public class PhoneNumberSelectionEngineRegistry extends DatasetSelectionEngineRegistry {

    public PhoneNumberSelectionEngineRegistry(
            AreaCodeSelectionEngine areaCodeSelector
    ) {
        super(
                null, null, null, null, // first/middle/last/nickname
                null, null, null, null, //streetName, streetSuffix, addressTwo, cityStateZip
                areaCodeSelector,
                null, null, // username, domain
                null // keyword
        );
    }
}
