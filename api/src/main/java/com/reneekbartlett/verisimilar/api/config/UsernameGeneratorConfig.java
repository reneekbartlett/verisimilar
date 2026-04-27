package com.reneekbartlett.verisimilar.api.config;

import com.reneekbartlett.verisimilar.core.generator.UsernameGenerator;
import com.reneekbartlett.verisimilar.core.selector.engine.KeywordSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsernameGeneratorConfig {

    @Bean
    public UsernameGenerator usernameGenerator(UsernameSelectionEngine usernameSelector, KeywordSelectionEngine keywordSelector) {
        return new UsernameGenerator(usernameSelector, keywordSelector);
    }
}
