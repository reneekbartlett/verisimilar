package com.reneekbartlett.verisimilar.api.config;

import com.reneekbartlett.verisimilar.core.generator.AsyncPersonGenerator;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneratePersonConfig {
//    @Bean
//    public PersonGenerator personRecordGenerator(
//            @Qualifier("datasetSelectionEngineRegistry") DatasetSelectionEngineRegistry datasetSelectionEngineRegistry
//    ) {
//        return new PersonGenerator(datasetSelectionEngineRegistry);
//    }

    @Bean
    public AsyncPersonGenerator asyncPersonRecordGenerator(
            @Qualifier("datasetSelectionEngineRegistry") DatasetSelectionEngineRegistry datasetSelectionEngineRegistry
    ) {
        return new AsyncPersonGenerator(datasetSelectionEngineRegistry);
    }
}
