package com.reneekbartlett.verisimilar.api.config;

import com.reneekbartlett.verisimilar.core.generator.PersonGenerator;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonGeneratorConfig {
    @Bean
    public PersonGenerator personRecordGenerator(
            DatasetResolverRegistry resolverRegistry
    ) {
        return new PersonGenerator(resolverRegistry);
    }
}
