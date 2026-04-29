package com.reneekbartlett.verisimilar.api.config;

import com.reneekbartlett.verisimilar.core.datasets.resolver.AddressTwoDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.AreaCodeDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.CityStateZipDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.DomainDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.FirstNameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.KeywordDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.LastNameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.MiddleNameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.NicknameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.StreetNameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.StreetSuffixDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.UsernameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.NameDatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatasetResolverConfig {

    @Bean(name="nameDatasetResolverRegistry")
    public DatasetResolverRegistry nameDatasetResolverRegistry() {
        ResourceLoaderUtil loader = new ResourceLoaderUtil();
        NameDatasetResolverRegistry registry = new NameDatasetResolverRegistry(
                new FirstNameDatasetResolver(loader),
                new MiddleNameDatasetResolver(loader),
                new LastNameDatasetResolver(loader),
                new NicknameDatasetResolver(loader)
        );
        return registry;
    }

    @Bean(name="datasetResolverRegistry")
    public DatasetResolverRegistry datasetResolverRegistry() {
        ResourceLoaderUtil loader = new ResourceLoaderUtil();

        FirstNameDatasetResolver firstNameResolver = new FirstNameDatasetResolver(loader);
        MiddleNameDatasetResolver middleNameResolver = new MiddleNameDatasetResolver(loader);
        LastNameDatasetResolver lastNameResolver = new LastNameDatasetResolver(loader);
        NicknameDatasetResolver nicknameResolver = new NicknameDatasetResolver(loader);

        StreetNameDatasetResolver streetNameResolver = new StreetNameDatasetResolver(loader);
        StreetSuffixDatasetResolver streetSuffixResolver = new StreetSuffixDatasetResolver(loader);
        AddressTwoDatasetResolver addressTwoResolver = new AddressTwoDatasetResolver(loader);
        CityStateZipDatasetResolver cityStateZipResolver = new CityStateZipDatasetResolver(loader);

        UsernameDatasetResolver usernameResolver = new UsernameDatasetResolver(loader);
        DomainDatasetResolver domainResolver = new DomainDatasetResolver(loader);

        AreaCodeDatasetResolver areaCodeResolver = new AreaCodeDatasetResolver(loader);

        KeywordDatasetResolver keywordResolver = new KeywordDatasetResolver(loader);

        return new DatasetResolverRegistry(
                firstNameResolver, 
                middleNameResolver, 
                lastNameResolver, 
                nicknameResolver,
                streetNameResolver, 
                streetSuffixResolver,
                addressTwoResolver, 
                cityStateZipResolver, 
                areaCodeResolver,
                usernameResolver, 
                domainResolver,
                keywordResolver
        );
    }
}
