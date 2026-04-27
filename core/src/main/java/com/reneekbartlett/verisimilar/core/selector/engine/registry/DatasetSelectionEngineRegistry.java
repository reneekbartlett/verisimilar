package com.reneekbartlett.verisimilar.core.selector.engine.registry;

import com.reneekbartlett.verisimilar.core.selector.engine.AddressTwoSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.AreaCodeSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.CityStateZipSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.FirstNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.KeywordSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.LastNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.MiddleNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.NicknameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetSuffixSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;

public class DatasetSelectionEngineRegistry {
    private final FirstNameSelectionEngine firstNameSelector;
    private final MiddleNameSelectionEngine middleNameSelector;
    private final LastNameSelectionEngine lastNameSelector;
    private final NicknameSelectionEngine nicknameSelector;
    private final StreetNameSelectionEngine streetNameSelector;
    private final StreetSuffixSelectionEngine streetSuffixSelector;
    private final AddressTwoSelectionEngine addressTwoSelector;
    private final CityStateZipSelectionEngine cityStateZipSelector;

    private final UsernameSelectionEngine usernameSelector;
    private final DomainSelectionEngine domainSelector;

    private final AreaCodeSelectionEngine areaCodeSelector;

    private final KeywordSelectionEngine keywordSelector;

    public DatasetSelectionEngineRegistry(
            FirstNameSelectionEngine firstNameSelector,
            MiddleNameSelectionEngine middleNameSelector,
            LastNameSelectionEngine lastNameSelector,
            NicknameSelectionEngine nicknameSelector,
            StreetNameSelectionEngine streetNameSelector,
            StreetSuffixSelectionEngine streetSuffixSelector,
            AddressTwoSelectionEngine addressTwoSelector,
            CityStateZipSelectionEngine cityStateZipSelector,

            AreaCodeSelectionEngine areaCodeSelector,

            UsernameSelectionEngine usernameSelector,
            DomainSelectionEngine domainSelector,

            KeywordSelectionEngine keywordSelector
    ) {
        this.firstNameSelector = firstNameSelector;
        this.middleNameSelector = middleNameSelector;
        this.lastNameSelector = lastNameSelector;
        this.nicknameSelector = nicknameSelector;

        this.streetNameSelector = streetNameSelector;
        this.streetSuffixSelector = streetSuffixSelector;
        this.addressTwoSelector = addressTwoSelector;
        this.cityStateZipSelector = cityStateZipSelector;
        
        this.areaCodeSelector = areaCodeSelector;

        this.usernameSelector = usernameSelector;
        this.domainSelector =  domainSelector;

        this.keywordSelector = keywordSelector;
    }

    public FirstNameSelectionEngine first() {
        return firstNameSelector;
    }

    public MiddleNameSelectionEngine middle() {
        return middleNameSelector;
    }

    public LastNameSelectionEngine last() {
        return lastNameSelector;
    }

    public NicknameSelectionEngine nickname() {
        return nicknameSelector;
    }

    public StreetNameSelectionEngine streetName() {
        return streetNameSelector;
    }

    public StreetSuffixSelectionEngine streetSuffix() {
        return streetSuffixSelector;
    }

    public AddressTwoSelectionEngine addressTwo() {
        return addressTwoSelector;
    }

    public CityStateZipSelectionEngine cityStateZip() {
        return cityStateZipSelector;
    }

    public UsernameSelectionEngine username() {
        return usernameSelector;
    }

    public DomainSelectionEngine domain() {
        return domainSelector;
    }

    public AreaCodeSelectionEngine areaCode() {
        return areaCodeSelector;
    }

    public KeywordSelectionEngine keyword() {
        return keywordSelector;
    }
}
