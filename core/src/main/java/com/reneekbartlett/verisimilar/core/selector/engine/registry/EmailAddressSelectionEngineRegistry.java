package com.reneekbartlett.verisimilar.core.selector.engine.registry;

import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.KeywordSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;

public class EmailAddressSelectionEngineRegistry extends DatasetSelectionEngineRegistry{
    public EmailAddressSelectionEngineRegistry(
            UsernameSelectionEngine usernameSelector,
            DomainSelectionEngine domainSelector,
            KeywordSelectionEngine keywordSelector
    ) {
        super(
                null, null, null, null, // first/middle/last/nickname
                null, null, null, null, //streetName, streetSuffix, addressTwo, cityStateZip
                null, // areaCode
                usernameSelector, domainSelector, 
                keywordSelector
        );
    }
}

