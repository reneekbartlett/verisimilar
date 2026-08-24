package com.reneekbartlett.verisimilar.core;

import com.reneekbartlett.verisimilar.core.datasets.AddressTwoFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.AreaCodeFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.CityStateZipFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.DatasetSource;
import com.reneekbartlett.verisimilar.core.datasets.DomainFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.FileDatasetMapperRegistry;
import com.reneekbartlett.verisimilar.core.datasets.FileDatasetSource;
import com.reneekbartlett.verisimilar.core.datasets.FirstNameFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.KeywordFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.LastNameFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.MiddleNameFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.NicknameFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.StreetNameFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.StreetSuffixFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.UsernameFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.key.AddressTwoDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.key.AreaCodeDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.key.CityStateZipDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.key.DomainDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.key.FirstNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.key.KeywordDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.key.LastNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.key.MiddleNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.key.NicknameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.key.StreetNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.key.StreetSuffixDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.key.UsernameDatasetKey;
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
import com.reneekbartlett.verisimilar.core.datasets.result.AddressTwoDatasetResult;
import com.reneekbartlett.verisimilar.core.datasets.result.AreaCodeDatasetResult;
import com.reneekbartlett.verisimilar.core.datasets.result.CityStateZipDatasetResult;
import com.reneekbartlett.verisimilar.core.datasets.result.DomainDatasetResult;
import com.reneekbartlett.verisimilar.core.datasets.result.FirstNameDatasetResult;
import com.reneekbartlett.verisimilar.core.datasets.result.KeywordDatasetResult;
import com.reneekbartlett.verisimilar.core.datasets.result.LastNameDatasetResult;
import com.reneekbartlett.verisimilar.core.datasets.result.MiddleNameDatasetResult;
import com.reneekbartlett.verisimilar.core.datasets.result.NicknameDatasetResult;
import com.reneekbartlett.verisimilar.core.datasets.result.StreetNameDatasetResult;
import com.reneekbartlett.verisimilar.core.datasets.result.StreetSuffixDatasetResult;
import com.reneekbartlett.verisimilar.core.datasets.result.UsernameDatasetResult;
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

    //private static final boolean testsCoreDisabled = PropertyLoader.getBoolean("tests.core.disabled", false);

    public static final SelectorStrategy<String> WEIGHTED_RANDOM = new WeightedSelectorStrategy<>();
    public static final SelectorStrategy<String> UNIFORM_RANDOM = new UniformSelectorStrategy<>();

    public static final String FIRST_NAME_FILEPATH_FMT = "datasets/cfg_full_name_first_name_%s_%s.csv";
    private static final String MIDDLE_NAME_FILEPATH_FMT = "datasets/cfg_full_name_middle_name_%s_%s.csv";
    private static final String LAST_NAME_FILEPATH_FMT = "datasets/cfg_full_name_last_name_%s.csv";
    private static final String NICKNAME_FILEPATH_FMT = "datasets/cfg_nickname_%s_%s.csv";

    private static final String STREET_NAME_FILEPATH_FMT = "datasets/cfg_postal_address_address1_street_name_%s.csv";
    private static final String STREET_SUFFIX_FILEPATH_FMT = "datasets/cfg_postal_address_address1_street_suffix_%s.csv";
    private static final String CITY_STATE_ZIP_FILEPATH_FMT = "datasets/cfg_postal_address_city_state_zip_us_state_%s.csv";

    private static final String ADDRESSTWO_FILEPATH_FMT = "datasets/cfg_postal_address_address2_unit_type_%s.csv";

    private static final String DOMAIN_FILEPATH_FMT = "datasets/cfg_domain_%s_2025.csv";
    private static final String USERNAME_FILEPATH_FMT = "datasets/cfg_username_keywords_%s.csv";

    private static final String AREA_CODE_FILEPATH_FMT = "datasets/cfg_phone_number_area_code_bystate_%s.csv";

    private static final String KEYWORD_FILEPATH_FMT = "datasets/cfg_username_keywords_%s.csv";

    private TestUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean isCoreTestingDisabled() {
        return false;
    }

    public static ResourceLoaderUtil getResourceLoader() {
        return new ResourceLoaderUtil();
    }

    public static FileDatasetMapperRegistry getFileDatasetMapperRegistry() {
        FileDatasetMapperRegistry registry = new FileDatasetMapperRegistry();

        registry.register(FirstNameDatasetKey.class, new FirstNameFileMapper());
        registry.register(MiddleNameDatasetKey.class, new MiddleNameFileMapper());
        registry.register(LastNameDatasetKey.class, new LastNameFileMapper());
        registry.register(NicknameDatasetKey.class, new NicknameFileMapper());

        registry.register(StreetNameDatasetKey.class, new StreetNameFileMapper());
        registry.register(StreetSuffixDatasetKey.class, new StreetSuffixFileMapper());
        registry.register(AddressTwoDatasetKey.class, new AddressTwoFileMapper());
        registry.register(CityStateZipDatasetKey.class, new CityStateZipFileMapper());

        registry.register(UsernameDatasetKey.class, new UsernameFileMapper());
        registry.register(DomainDatasetKey.class, new DomainFileMapper());

        registry.register(AreaCodeDatasetKey.class, new AreaCodeFileMapper());

        registry.register(KeywordDatasetKey.class, new KeywordFileMapper());

        return registry;
    }

    public static DatasetResolverRegistry getDatasetResolverRegistry() {
        ResourceLoaderUtil loader = new ResourceLoaderUtil();

        DatasetSource<FirstNameDatasetKey, FirstNameDatasetResult> firstNameFileSrc =
                new FileDatasetSource<>(loader, new FirstNameFileMapper(FIRST_NAME_FILEPATH_FMT));
        DatasetSource<MiddleNameDatasetKey, MiddleNameDatasetResult> middleNameFileSrc =
                new FileDatasetSource<>(loader, new MiddleNameFileMapper(MIDDLE_NAME_FILEPATH_FMT));
        DatasetSource<LastNameDatasetKey, LastNameDatasetResult> lastNameFileSrc =
                new FileDatasetSource<>(loader, new LastNameFileMapper(LAST_NAME_FILEPATH_FMT));
        DatasetSource<NicknameDatasetKey, NicknameDatasetResult> nicknameFileSrc =
                new FileDatasetSource<>(loader, new NicknameFileMapper(NICKNAME_FILEPATH_FMT));

        FirstNameDatasetResolver firstNameResolver = new FirstNameDatasetResolver(firstNameFileSrc);
        MiddleNameDatasetResolver middleNameResolver = new MiddleNameDatasetResolver(middleNameFileSrc);
        LastNameDatasetResolver lastNameResolver = new LastNameDatasetResolver(lastNameFileSrc);
        NicknameDatasetResolver nicknameResolver = new NicknameDatasetResolver(nicknameFileSrc);

        DatasetSource<StreetNameDatasetKey, StreetNameDatasetResult> streetNameFileSource =
                new FileDatasetSource<>(loader, new StreetNameFileMapper(STREET_NAME_FILEPATH_FMT));
        DatasetSource<StreetSuffixDatasetKey, StreetSuffixDatasetResult> streetSuffixFileSource =
                new FileDatasetSource<>(loader, new StreetSuffixFileMapper(STREET_SUFFIX_FILEPATH_FMT));
        DatasetSource<AddressTwoDatasetKey, AddressTwoDatasetResult> addressTwoFileSource =
                new FileDatasetSource<>(loader, new AddressTwoFileMapper(ADDRESSTWO_FILEPATH_FMT));
        DatasetSource<CityStateZipDatasetKey, CityStateZipDatasetResult> cityStateZipFileSource =
                new FileDatasetSource<>(loader, new CityStateZipFileMapper(CITY_STATE_ZIP_FILEPATH_FMT));

        StreetNameDatasetResolver streetNameResolver = new StreetNameDatasetResolver(streetNameFileSource);
        StreetSuffixDatasetResolver streetSuffixResolver = new StreetSuffixDatasetResolver(streetSuffixFileSource);
        AddressTwoDatasetResolver addressTwoResolver = new AddressTwoDatasetResolver(addressTwoFileSource);
        CityStateZipDatasetResolver cityStateZipResolver = new CityStateZipDatasetResolver(cityStateZipFileSource);

        DatasetSource<UsernameDatasetKey, UsernameDatasetResult> usernameFileSource =
                new FileDatasetSource<>(loader, new UsernameFileMapper(USERNAME_FILEPATH_FMT));
        DatasetSource<DomainDatasetKey, DomainDatasetResult> domainFileSource =
                new FileDatasetSource<>(loader, new DomainFileMapper(DOMAIN_FILEPATH_FMT));

        UsernameDatasetResolver usernameResolver = new UsernameDatasetResolver(usernameFileSource);
        DomainDatasetResolver domainResolver = new DomainDatasetResolver(domainFileSource);

        DatasetSource<AreaCodeDatasetKey, AreaCodeDatasetResult> areaCodeFileSource =
                new FileDatasetSource<>(loader, new AreaCodeFileMapper(AREA_CODE_FILEPATH_FMT));
        AreaCodeDatasetResolver areaCodeResolver = new AreaCodeDatasetResolver(areaCodeFileSource);

        DatasetSource<KeywordDatasetKey, KeywordDatasetResult> keywordFileSource =
                new FileDatasetSource<>(loader, new KeywordFileMapper(KEYWORD_FILEPATH_FMT));
        KeywordDatasetResolver keywordResolver = new KeywordDatasetResolver(keywordFileSource);

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

        DatasetSource<UsernameDatasetKey, UsernameDatasetResult> usernameFileSource =
                new FileDatasetSource<>(loader, new UsernameFileMapper(USERNAME_FILEPATH_FMT));
        DatasetSource<DomainDatasetKey, DomainDatasetResult> domainFileSource =
                new FileDatasetSource<>(loader, new DomainFileMapper(DOMAIN_FILEPATH_FMT));
        DatasetSource<KeywordDatasetKey, KeywordDatasetResult> keywordFileSource =
                new FileDatasetSource<>(loader, new KeywordFileMapper(KEYWORD_FILEPATH_FMT));

        UsernameDatasetResolver usernameResolver = new UsernameDatasetResolver(usernameFileSource);
        DomainDatasetResolver domainResolver = new DomainDatasetResolver(domainFileSource);
        KeywordDatasetResolver keywordResolver = new KeywordDatasetResolver(keywordFileSource);

        return new EmailAddressDatasetResolverRegistry(usernameResolver, domainResolver, keywordResolver);
    }

    public static PhoneNumberDatasetResolverRegistry getPhoneNumberDatasetResolverRegistry() {
        ResourceLoaderUtil loader = new ResourceLoaderUtil();
        DatasetSource<AreaCodeDatasetKey, AreaCodeDatasetResult> areaCodeFileSource =
                new FileDatasetSource<>(loader, new AreaCodeFileMapper(AREA_CODE_FILEPATH_FMT));
        AreaCodeDatasetResolver areaCodeResolver = new AreaCodeDatasetResolver(areaCodeFileSource);
        return new PhoneNumberDatasetResolverRegistry(areaCodeResolver);
    }

    public static PostalAddressDatasetResolverRegistry getPostalAddressDatasetResolverRegistry() {
        ResourceLoaderUtil loader = new ResourceLoaderUtil();

        DatasetSource<StreetNameDatasetKey, StreetNameDatasetResult> streetNameFileSource =
                new FileDatasetSource<>(loader, new StreetNameFileMapper(STREET_NAME_FILEPATH_FMT));
        DatasetSource<StreetSuffixDatasetKey, StreetSuffixDatasetResult> streetSuffixFileSource =
                new FileDatasetSource<>(loader, new StreetSuffixFileMapper(STREET_SUFFIX_FILEPATH_FMT));
        DatasetSource<AddressTwoDatasetKey, AddressTwoDatasetResult> addressTwoFileSource =
                new FileDatasetSource<>(loader, new AddressTwoFileMapper(ADDRESSTWO_FILEPATH_FMT));
        DatasetSource<CityStateZipDatasetKey, CityStateZipDatasetResult> cityStateZipFileSource =
                new FileDatasetSource<>(loader, new CityStateZipFileMapper(CITY_STATE_ZIP_FILEPATH_FMT));

        StreetNameDatasetResolver streetNameResolver = new StreetNameDatasetResolver(streetNameFileSource);
        StreetSuffixDatasetResolver streetSuffixResolver = new StreetSuffixDatasetResolver(streetSuffixFileSource);
        AddressTwoDatasetResolver addressTwoResolver = new AddressTwoDatasetResolver(addressTwoFileSource);
        CityStateZipDatasetResolver cityStateZipResolver = new CityStateZipDatasetResolver(cityStateZipFileSource);

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

        DatasetSource<FirstNameDatasetKey, FirstNameDatasetResult> firstNameFileSrc =
                new FileDatasetSource<>(loader, new FirstNameFileMapper(FIRST_NAME_FILEPATH_FMT));
        DatasetSource<MiddleNameDatasetKey, MiddleNameDatasetResult> middleNameFileSrc =
                new FileDatasetSource<>(loader, new MiddleNameFileMapper(MIDDLE_NAME_FILEPATH_FMT));
        DatasetSource<LastNameDatasetKey, LastNameDatasetResult> lastNameFileSrc =
                new FileDatasetSource<>(loader, new LastNameFileMapper(LAST_NAME_FILEPATH_FMT));
        DatasetSource<NicknameDatasetKey, NicknameDatasetResult> nicknameFileSrc =
                new FileDatasetSource<>(loader, new NicknameFileMapper(NICKNAME_FILEPATH_FMT));

        FirstNameDatasetResolver firstNameResolver = new FirstNameDatasetResolver(firstNameFileSrc);
        MiddleNameDatasetResolver middleNameResolver = new MiddleNameDatasetResolver(middleNameFileSrc);
        LastNameDatasetResolver lastNameResolver = new LastNameDatasetResolver(lastNameFileSrc);
        NicknameDatasetResolver nicknameResolver = new NicknameDatasetResolver(nicknameFileSrc);
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
