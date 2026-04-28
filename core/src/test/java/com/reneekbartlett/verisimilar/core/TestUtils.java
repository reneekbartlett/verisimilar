package com.reneekbartlett.verisimilar.core;

import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.resolver.AddressTwoDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.AreaCodeDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.CityStateZipDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.DomainDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.FirstNameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.KeywordDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.LastNameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.MiddleNameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.NicknameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.StreetNameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.StreetSuffixDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.UsernameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.EmailAddressDatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.NameDatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.PhoneNumberDatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.PostalAddressDatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.generator.CityStateZipGenerator;
import com.reneekbartlett.verisimilar.core.generator.FirstNameGenerator;
import com.reneekbartlett.verisimilar.core.generator.LastNameGenerator;
import com.reneekbartlett.verisimilar.core.generator.MiddleNameGenerator;
import com.reneekbartlett.verisimilar.core.generator.StreetAddressGenerator;
import com.reneekbartlett.verisimilar.core.generator.registry.NameGeneratorRegistry;
import com.reneekbartlett.verisimilar.core.generator.registry.PostalAddressGeneratorRegistry;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.UniformSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.engine.AddressTwoSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.AreaCodeSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.CityStateZipSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.FirstNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.KeywordSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.LastNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.MiddleNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.NicknameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetSuffixSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.EmailAddressSelectionEngineRegistry;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.PhoneNumberSelectionEngineRegistry;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.PostalAddressSelectionEngineRegistry;
//import com.reneekbartlett.verisimilar.core.selector.engine.registry.NameSelectionEngineRegistry;
import com.reneekbartlett.verisimilar.core.templates.TemplateRegistry;
import com.reneekbartlett.verisimilar.core.templates.loader.TemplateRegistryLoader;

public class TestUtils {

    public static final SelectorStrategy<String> WEIGHTED_RANDOM = new WeightedSelectorStrategy<>();
    public static final SelectorStrategy<String> UNIFORM_RANDOM = new UniformSelectorStrategy<>();

    public static ResourceLoaderUtil getResourceLoader() {
        return new ResourceLoaderUtil();
    }

    public static DatasetResolverRegistry getDatasetResolverRegistry() {
        ResourceLoaderUtil loader = new ResourceLoaderUtil();

        FirstNameDatasetResolver firstNameResolver = new FirstNameDatasetResolver(loader);
        MiddleNameDatasetResolver middleNameResolver = new MiddleNameDatasetResolver(loader);
        LastNameDatasetResolver lastNameResolver = new LastNameDatasetResolver(loader);
        NicknameDatasetResolver nicknameResolver = new NicknameDatasetResolver(loader);

        StreetNameDatasetResolver streetNameResolver = new StreetNameDatasetResolver(loader);
        StreetSuffixDatasetResolver streetSuffixResolver = new StreetSuffixDatasetResolver(loader);
        AddressTwoDatasetResolver addressTwoResolver = new AddressTwoDatasetResolver(loader);
        CityStateZipDatasetResolver cityStateZipResolver = new CityStateZipDatasetResolver(loader);

        UsernameDatasetResolver usernameResolver = new UsernameDatasetResolver(loader);

        DomainDatasetResolver domainResolver = new DomainDatasetResolver(loader);

        AreaCodeDatasetResolver areaCodeResolver = new AreaCodeDatasetResolver(loader);

        KeywordDatasetResolver keywordResolver = new KeywordDatasetResolver(loader);

        return new DatasetResolverRegistry(
                firstNameResolver, middleNameResolver, lastNameResolver, nicknameResolver,
                streetNameResolver, streetSuffixResolver,
                addressTwoResolver, cityStateZipResolver, 
                areaCodeResolver,
                usernameResolver, 
                domainResolver,
                keywordResolver
        );
    }

    public static EmailAddressDatasetResolverRegistry getEmailAddressDatasetResolverRegistry() {
        ResourceLoaderUtil loader = new ResourceLoaderUtil();
        UsernameDatasetResolver usernameResolver = new UsernameDatasetResolver(loader);
        DomainDatasetResolver domainResolver = new DomainDatasetResolver(loader);
        KeywordDatasetResolver keywordResolver = new KeywordDatasetResolver(loader);
        return new EmailAddressDatasetResolverRegistry(usernameResolver, domainResolver, keywordResolver);
    }

    public static PhoneNumberDatasetResolverRegistry getPhoneNumberDatasetResolverRegistry() {
        ResourceLoaderUtil loader = new ResourceLoaderUtil();
        AreaCodeDatasetResolver areaCodeResolver = new AreaCodeDatasetResolver(loader);
        return new PhoneNumberDatasetResolverRegistry(areaCodeResolver);
    }

    public static PostalAddressDatasetResolverRegistry getPostalAddressDatasetResolverRegistry() {
        ResourceLoaderUtil loader = new ResourceLoaderUtil();
        StreetNameDatasetResolver streetNameResolver = new StreetNameDatasetResolver(loader);
        StreetSuffixDatasetResolver streetSuffixResolver = new StreetSuffixDatasetResolver(loader);
        AddressTwoDatasetResolver addressTwoResolver = new AddressTwoDatasetResolver(loader);
        CityStateZipDatasetResolver cityStateZipResolver = new CityStateZipDatasetResolver(loader);
        return new PostalAddressDatasetResolverRegistry(streetNameResolver, streetSuffixResolver, addressTwoResolver, cityStateZipResolver);
    }

    public static DatasetSelectionEngineRegistry getDatasetSelectionEngineRegistry() {
        DatasetResolverRegistry resolvers = getDatasetResolverRegistry();

        FirstNameSelectionEngine firstNameSelector = new FirstNameSelectionEngine(resolvers);
        MiddleNameSelectionEngine middleNameSelector = new MiddleNameSelectionEngine(resolvers);
        LastNameSelectionEngine lastNameSelector = new LastNameSelectionEngine(resolvers);
        NicknameSelectionEngine nicknameSelector = new NicknameSelectionEngine(resolvers);

        StreetNameSelectionEngine streetNameSelector = new StreetNameSelectionEngine(resolvers);
        StreetSuffixSelectionEngine streetSuffixSelector = new StreetSuffixSelectionEngine(resolvers);
        AddressTwoSelectionEngine addressTwoSelector = new AddressTwoSelectionEngine(resolvers);
        CityStateZipSelectionEngine cityStateZipSelector = new CityStateZipSelectionEngine(resolvers);

        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(resolvers);
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(resolvers, WEIGHTED_RANDOM);

        AreaCodeSelectionEngine areaCodeSelector = new AreaCodeSelectionEngine(resolvers, WEIGHTED_RANDOM);

        KeywordSelectionEngine keywordSelector = new KeywordSelectionEngine(resolvers, WEIGHTED_RANDOM);

        return new DatasetSelectionEngineRegistry(
                firstNameSelector,
                middleNameSelector, 
                lastNameSelector,
                nicknameSelector,
                streetNameSelector,
                streetSuffixSelector,
                addressTwoSelector,
                cityStateZipSelector,
                areaCodeSelector,
                usernameSelector,
                domainSelector,
                keywordSelector
        );
    }

    public static TemplateRegistry getUsernameTemplateRegistry() {
        TemplateRegistryLoader templateLoader = new TemplateRegistryLoader();
        TemplateRegistry templateRegistry = templateLoader.loadFromClasspath("templates/username-templates.yaml");
        return templateRegistry;
    }

    public static EmailAddressSelectionEngineRegistry getEmailAddressSelectionEngineRegistry() {
        DatasetResolverRegistry emailResolvers = getEmailAddressDatasetResolverRegistry();

        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(emailResolvers);
        DomainSelectionEngine domainSelector = new DomainSelectionEngine(emailResolvers, WEIGHTED_RANDOM);
        KeywordSelectionEngine keywordSelector = new KeywordSelectionEngine(emailResolvers, WEIGHTED_RANDOM);
        return new EmailAddressSelectionEngineRegistry(usernameSelector, domainSelector, keywordSelector);
    }

    public static PostalAddressSelectionEngineRegistry getPostalAddressSelectionEngineRegistry() {
        DatasetResolverRegistry resolvers = getDatasetResolverRegistry();
        StreetNameSelectionEngine streetNameSelector = new StreetNameSelectionEngine(resolvers, WEIGHTED_RANDOM);
        StreetSuffixSelectionEngine streetSuffixSelector = new StreetSuffixSelectionEngine(resolvers, WEIGHTED_RANDOM);
        AddressTwoSelectionEngine addressTwoSelector = new AddressTwoSelectionEngine(resolvers, WEIGHTED_RANDOM);
        CityStateZipSelectionEngine cityStateZipSelector = new CityStateZipSelectionEngine(resolvers, WEIGHTED_RANDOM);
        return new PostalAddressSelectionEngineRegistry(streetNameSelector, streetSuffixSelector, addressTwoSelector, cityStateZipSelector);
    }

    public static PhoneNumberSelectionEngineRegistry getPhoneNumberSelectionEngineRegistry() {
        DatasetResolverRegistry resolvers = getDatasetResolverRegistry();
        AreaCodeSelectionEngine areaCodeSelector = new AreaCodeSelectionEngine(resolvers, WEIGHTED_RANDOM);
        return new PhoneNumberSelectionEngineRegistry(areaCodeSelector);
    }

    /***
     * DatasetResolverRegistry
     * 
     */
    public static NameDatasetResolverRegistry getNameDatasetResolverRegistry() {
        ResourceLoaderUtil loader = new ResourceLoaderUtil();
        FirstNameDatasetResolver firstNameResolver = new FirstNameDatasetResolver(loader);
        MiddleNameDatasetResolver middleNameResolver = new MiddleNameDatasetResolver(loader);
        LastNameDatasetResolver lastNameResolver = new LastNameDatasetResolver(loader);
        NicknameDatasetResolver nicknameResolver = new NicknameDatasetResolver(loader);
        return new NameDatasetResolverRegistry(firstNameResolver, middleNameResolver, lastNameResolver, nicknameResolver);
    }

    /***
     * GeneratorRegistry
     * 
     */
    public NameGeneratorRegistry getNameGeneratorRegistry() {
        DatasetResolverRegistry resolvers = getDatasetResolverRegistry();

        FirstNameSelectionEngine firstNameSelector = new FirstNameSelectionEngine(resolvers, WEIGHTED_RANDOM);
        FirstNameGenerator firstNameGenerator = new FirstNameGenerator(firstNameSelector);

        MiddleNameSelectionEngine middleNameSelector = new MiddleNameSelectionEngine(resolvers, WEIGHTED_RANDOM);
        MiddleNameGenerator middleNameGenerator = new MiddleNameGenerator(middleNameSelector);

        LastNameSelectionEngine lastNameSelector = new LastNameSelectionEngine(resolvers, WEIGHTED_RANDOM);
        LastNameGenerator lastNameGenerator = new LastNameGenerator(lastNameSelector);

        return new NameGeneratorRegistry(firstNameGenerator, middleNameGenerator, lastNameGenerator);
    }

    public PostalAddressGeneratorRegistry getPostalAddressGeneratorRegistry(PostalAddressSelectionEngineRegistry SelectorRegistry) {
        StreetNameSelectionEngine streetNameSelector = SelectorRegistry.streetName();
        StreetSuffixSelectionEngine streetSuffixSelector = SelectorRegistry.streetSuffix();
        AddressTwoSelectionEngine addressTwoSelector = SelectorRegistry.addressTwo();

        StreetAddressGenerator streetAddressGenerator = new StreetAddressGenerator(streetNameSelector, streetSuffixSelector, addressTwoSelector);
        CityStateZipGenerator cityStateZipGenerator = new CityStateZipGenerator(SelectorRegistry.cityStateZip());

        return new PostalAddressGeneratorRegistry(streetAddressGenerator, cityStateZipGenerator);
    }
}
