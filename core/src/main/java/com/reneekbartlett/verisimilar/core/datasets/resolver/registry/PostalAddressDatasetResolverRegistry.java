package com.reneekbartlett.verisimilar.core.datasets.resolver.registry;

import com.reneekbartlett.verisimilar.core.datasets.resolver.AddressTwoDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.CityStateZipDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.StreetNameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.StreetSuffixDatasetResolver;

public class PostalAddressDatasetResolverRegistry extends DatasetResolverRegistry {

    public PostalAddressDatasetResolverRegistry(
            StreetNameDatasetResolver streetNameResolver,
            StreetSuffixDatasetResolver streetSuffixResolver,
            AddressTwoDatasetResolver addressTwoResolver,
            CityStateZipDatasetResolver cityStateZipResolver
    ) {
        super(
                null, null, null, null,
                streetNameResolver,
                streetSuffixResolver,
                addressTwoResolver,
                cityStateZipResolver,
                null,
                null,
                null,
                null
        );
    }
}
