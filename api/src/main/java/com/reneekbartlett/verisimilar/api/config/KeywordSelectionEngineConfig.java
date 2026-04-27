package com.reneekbartlett.verisimilar.api.config;

import com.reneekbartlett.verisimilar.core.selector.engine.KeywordSelectionEngine;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeywordSelectionEngineConfig {

    @Bean(name="keywordSelectionEngine")
    public KeywordSelectionEngine usernameSelectionEngine(
            @Qualifier("datasetResolverRegistry") DatasetResolverRegistry resolverRegistry
    ) {
        return new KeywordSelectionEngine(resolverRegistry);
    }
}
