package com.reneekbartlett.verisimilar.core.datasets.resolver.registry;

import com.reneekbartlett.verisimilar.core.datasets.resolver.FirstNameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.LastNameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.MiddleNameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.NicknameDatasetResolver;

public class NameDatasetResolverRegistry extends DatasetResolverRegistry {

    public NameDatasetResolverRegistry(
            FirstNameDatasetResolver firstNameResolver,
            MiddleNameDatasetResolver middleNameResolver,
            LastNameDatasetResolver lastNameResolver,
            NicknameDatasetResolver nicknameResolver
    ) {
        super(firstNameResolver, middleNameResolver, lastNameResolver, 
                nicknameResolver,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
