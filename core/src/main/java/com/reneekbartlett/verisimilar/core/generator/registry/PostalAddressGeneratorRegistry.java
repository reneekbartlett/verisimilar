package com.reneekbartlett.verisimilar.core.generator.registry;

import com.reneekbartlett.verisimilar.core.generator.CityStateZipGenerator;
import com.reneekbartlett.verisimilar.core.generator.StreetAddressGenerator;

public class PostalAddressGeneratorRegistry {
    private final StreetAddressGenerator streetAddressGenerator;
    private final CityStateZipGenerator cityStateZipGenerator;

    public PostalAddressGeneratorRegistry(
            StreetAddressGenerator streetAddressGenerator,
            CityStateZipGenerator cityStateZipGenerator
    ) {
        this.streetAddressGenerator = streetAddressGenerator;
        this.cityStateZipGenerator = cityStateZipGenerator;
    }

    public StreetAddressGenerator streetAddress() {
        return streetAddressGenerator;
    }

    public CityStateZipGenerator cityStateZip() {
        return cityStateZipGenerator;
    }
}
