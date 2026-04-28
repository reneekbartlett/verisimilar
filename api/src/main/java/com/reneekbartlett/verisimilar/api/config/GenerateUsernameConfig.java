package com.reneekbartlett.verisimilar.api.config;

import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.generator.UsernameGenerator;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenerateUsernameConfig {

    @Bean(name="usernameSelectionEngine")
    public UsernameSelectionEngine usernameSelectionEngine(
            @Qualifier("datasetResolverRegistry") DatasetResolverRegistry resolverRegistry
    ) {
        return new UsernameSelectionEngine(resolverRegistry, UsernameSelectionEngine.defaultSelectorStrategy());
    }

//    @Bean(name="usernameTemplateRegistry")
//    public TemplateRegistry usernameTemplateRegistry() {
//        TemplateRegistryLoader loader = new TemplateRegistryLoader();
//        return loader.loadFromClasspath("templates/username-templates.yaml");
//    }

    @Bean
    public UsernameGenerator usernameGenerator(
            @Qualifier("usernameSelectionEngine") UsernameSelectionEngine usernameSelector
            //,@Qualifier("keywordSelectionEngine") KeywordSelectionEngine keywordSelector
    ) {
        return new UsernameGenerator(usernameSelector);
    }
}
