package com.reneekbartlett.verisimilar.api.config;

import com.reneekbartlett.verisimilar.core.generator.EmailAddressGenerator;
import com.reneekbartlett.verisimilar.core.generator.FullNameGenerator;
import com.reneekbartlett.verisimilar.core.generator.PhoneNumberGenerator;
import com.reneekbartlett.verisimilar.core.generator.PostalAddressRecordGenerator;
//import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.generator.UsernameGenerator;
import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.FirstNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.KeywordSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.LastNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.MiddleNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneratorConfig {

    @Bean
    public UsernameGenerator usernameGenerator(
            @Qualifier("usernameSelectionEngine") UsernameSelectionEngine usernameSelector
    ) {
        return new UsernameGenerator(usernameSelector);
    }

    @Bean
    public EmailAddressGenerator emailAddressGenerator(
            @Qualifier("usernameSelectionEngine") UsernameSelectionEngine usernameSelector,
            @Qualifier("domainSelectionEngine") DomainSelectionEngine domainSelector,
            @Qualifier("keywordSelectionEngine") KeywordSelectionEngine keywordSelector
    ) {
        return new EmailAddressGenerator(usernameSelector, domainSelector, keywordSelector);
    }

    @Bean
    public FullNameGenerator fullNameGenerator(
            @Qualifier("firstNameSelectionEngine") FirstNameSelectionEngine firstNameSelector,
            @Qualifier("middleNameSelectionEngine") MiddleNameSelectionEngine middleNameSelector,
            @Qualifier("lastNameSelectionEngine") LastNameSelectionEngine lastNameSelector
    ) {
        return new FullNameGenerator(firstNameSelector, middleNameSelector, lastNameSelector);
    }

    @Bean
    public PostalAddressRecordGenerator postalAddressGenerator(
            @Qualifier("datasetSelectionEngineRegistry") DatasetSelectionEngineRegistry datasetSelectionEngineRegistry
    ) {
        return new PostalAddressRecordGenerator(datasetSelectionEngineRegistry);
    }

    @Bean
    public PhoneNumberGenerator phoneNumberGenerator(
            @Qualifier("datasetSelectionEngineRegistry") DatasetSelectionEngineRegistry datasetSelectionEngineRegistry
    ) {
        return new PhoneNumberGenerator(datasetSelectionEngineRegistry);
    }
}
