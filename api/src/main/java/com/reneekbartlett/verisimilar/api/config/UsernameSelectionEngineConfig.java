package com.reneekbartlett.verisimilar.api.config;

import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.templates.TemplateRegistry;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsernameSelectionEngineConfig {

//    @Bean
//    public TemplateRegistry usernameTemplateRegistry(
//            Set<TemplateField> usableTemplateFields
//    ) {
//        usableTemplateFields.add(TemplateField.KEYWORD);
//
//        Map<Set<TemplateField>, TemplateSet> templateMap = new HashMap<>();
//        //TemplateSet.of("${KEYWORD}${SEPARATOR}${NUM1000}","${KEYWORD}${NUM10}","${KEYWORD}${NUM100}","${KEYWORD}${NUM1000}");
//
//        return new TemplateRegistry(templateMap);
//    }

    @Bean(name="usernameSelectionEngine")
    public UsernameSelectionEngine usernameSelectionEngine(
            @Qualifier("datasetResolverRegistry") DatasetResolverRegistry resolverRegistry,
            TemplateRegistry usernameTemplateRegistry
    ) {
        return new UsernameSelectionEngine(resolverRegistry, UsernameSelectionEngine.defaultSelectorStrategy(), usernameTemplateRegistry);
    }
}
