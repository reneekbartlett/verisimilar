package com.reneekbartlett.verisimilar.core.datasets.resolver.registry;

import com.reneekbartlett.verisimilar.core.datasets.resolver.AreaCodeDatasetResolver;

public class PhoneNumberDatasetResolverRegistry extends DatasetResolverRegistry {

    public PhoneNumberDatasetResolverRegistry(
            AreaCodeDatasetResolver areaCodeResolver
    ) {
        super(
                null, null, null, null, // first, middle, last, nickname
                null, null, // streetname, streetsuffix
                null, // address2
                null, //citystatezip
                areaCodeResolver,
                null, // username
                null, // domain
                null // keyword
        );
    }
}
