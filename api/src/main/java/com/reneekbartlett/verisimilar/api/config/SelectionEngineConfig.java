package com.reneekbartlett.verisimilar.api.config;

import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.FirstNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.KeywordSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.LastNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.MiddleNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SelectionEngineConfig {
    @Bean(name="datasetSelectionEngineRegistry")
    public DatasetSelectionEngineRegistry datasetSelectionEngineRegistry(
            @Qualifier("datasetResolverRegistry") DatasetResolverRegistry resolverRegistry
    ) {
        return resolverRegistry.selectors();
    }

    // TODO:  Change to DatasetSelectionEngineRegistry?
    @Bean(name="keywordSelectionEngine")
    public KeywordSelectionEngine keywordSelectionEngine(
            @Qualifier("datasetResolverRegistry") DatasetResolverRegistry resolverRegistry
    ) {
        return new KeywordSelectionEngine(resolverRegistry);
    }

    @Bean(name="firstNameSelectionEngine")
    public FirstNameSelectionEngine firstNameSelectionEngine(
            @Qualifier("datasetSelectionEngineRegistry") DatasetSelectionEngineRegistry datasetSelectionEngineRegistry
    ) {
        return datasetSelectionEngineRegistry.first();
    }

    @Bean(name="middleNameSelectionEngine")
    public MiddleNameSelectionEngine middleNameSelectionEngine(
            @Qualifier("datasetSelectionEngineRegistry") DatasetSelectionEngineRegistry datasetSelectionEngineRegistry
    ) {
        return datasetSelectionEngineRegistry.middle();
    }

    @Bean(name="lastNameSelectionEngine")
    public LastNameSelectionEngine lastNameSelectionEngine(
            @Qualifier("datasetSelectionEngineRegistry") DatasetSelectionEngineRegistry datasetSelectionEngineRegistry
    ) {
        return datasetSelectionEngineRegistry.last();
    }

    @Bean(name="usernameSelectionEngine")
    public UsernameSelectionEngine usernameSelectionEngine(
            @Qualifier("datasetSelectionEngineRegistry") DatasetSelectionEngineRegistry datasetSelectionEngineRegistry
    ) {
        return datasetSelectionEngineRegistry.username();
    }

    @Bean(name="domainSelectionEngine")
    public DomainSelectionEngine domainSelectionEngine(
            @Qualifier("datasetSelectionEngineRegistry") DatasetSelectionEngineRegistry datasetSelectionEngineRegistry
    ) {
        return datasetSelectionEngineRegistry.domain();
    }
}
