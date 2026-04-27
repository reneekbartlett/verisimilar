package com.reneekbartlett.verisimilar.core.datasets.resolver.registry;

import com.reneekbartlett.verisimilar.core.datasets.resolver.DomainDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.KeywordDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.UsernameDatasetResolver;

public class EmailAddressDatasetResolverRegistry extends DatasetResolverRegistry {

    public EmailAddressDatasetResolverRegistry(
            UsernameDatasetResolver usernameResolver,
            DomainDatasetResolver domainResolver,
            KeywordDatasetResolver keywordResolver
    ) {
        super(null, null, null, null,
                null,
                null,
                null,
                null,
                null,
                usernameResolver,
                domainResolver,
                keywordResolver
        );
    }
}