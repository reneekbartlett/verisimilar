package com.reneekbartlett.verisimilar.api.config;

import com.reneekbartlett.verisimilar.core.generator.PersonGenerator;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneratePersonConfig {
    @Bean
    public PersonGenerator personRecordGenerator(
            @Qualifier("datasetResolverRegistry") DatasetResolverRegistry resolverRegistry
    ) {
        return new PersonGenerator(resolverRegistry);
    }
}
