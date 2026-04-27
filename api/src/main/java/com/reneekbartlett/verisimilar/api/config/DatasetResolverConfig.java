package com.reneekbartlett.verisimilar.api.config;

import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.NameDatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.datasets.resolver.*;
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

    //@Bean
    //public DatasetResolverRegistry datasetResolverRegistry() {
    //    DatasetResolverRegistry registry = new DatasetResolverRegistry();
    //    registry.register(new UsernameDatasetResolver(
    //            new ResourceLoaderUtil(),
    //            /* allTemplates loaded by TemplateRegistryLoader, but resolver only needs raw templates */
    //            Set.of() // or inject if needed
    //    ));
    //    return registry;
    //}
}
