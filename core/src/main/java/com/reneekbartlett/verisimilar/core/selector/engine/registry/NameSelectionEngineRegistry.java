package com.reneekbartlett.verisimilar.core.selector.engine.registry;

import com.reneekbartlett.verisimilar.core.selector.engine.FirstNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.LastNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.MiddleNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.NicknameSelectionEngine;

public class NameSelectionEngineRegistry extends DatasetSelectionEngineRegistry {

    public NameSelectionEngineRegistry(
            FirstNameSelectionEngine firstNameSelector,
            MiddleNameSelectionEngine middleNameSelector,
            LastNameSelectionEngine lastNameSelector,
            NicknameSelectionEngine nicknameSelector
    ) {
        super(
                firstNameSelector, middleNameSelector, lastNameSelector, nicknameSelector,
                null, null, null, null, //streetName, streetSuffix, addressTwo, cityStateZip
                null, // areaCode
                null, null, // username, domain
                null // keyword
        );
    }
}
