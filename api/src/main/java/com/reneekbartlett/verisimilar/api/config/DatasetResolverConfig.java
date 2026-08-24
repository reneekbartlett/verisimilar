package com.reneekbartlett.verisimilar.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reneekbartlett.verisimilar.core.datasets.AddressTwoFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.AreaCodeFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.DatasetSource;
import com.reneekbartlett.verisimilar.core.datasets.DomainFileMapper;
//import com.reneekbartlett.verisimilar.core.datasets.FileDatasetMapper;
import com.reneekbartlett.verisimilar.core.datasets.FileDatasetMapperRegistry;
import com.reneekbartlett.verisimilar.core.datasets.FileDatasetSource;
import com.reneekbartlett.verisimilar.core.datasets.FirstNameFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.KeywordFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.LastNameFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.MiddleNameFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.NicknameFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.StreetNameFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.StreetSuffixFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.CityStateZipFileMapper;
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
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.NameDatasetResolverRegistry;
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

@Configuration
public class DatasetResolverConfig {

    private static final String FIRST_NAME_FILEPATH_FMT = "datasets/cfg_full_name_first_name_%s_%s.csv";
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

    // KEYWORDS "datasets/cfg_username_keywords_%s.csv"

    // TODO: 
    @Bean(name="fileDatasetMapperRegistry")
    public FileDatasetMapperRegistry fileDatasetMapperRegistry() {
        FileDatasetMapperRegistry registry = new FileDatasetMapperRegistry();
        registry.register(FirstNameDatasetKey.class, new FirstNameFileMapper(FIRST_NAME_FILEPATH_FMT));
        registry.register(MiddleNameDatasetKey.class, new MiddleNameFileMapper(MIDDLE_NAME_FILEPATH_FMT));
        registry.register(LastNameDatasetKey.class, new LastNameFileMapper(LAST_NAME_FILEPATH_FMT));
        registry.register(NicknameDatasetKey.class, new NicknameFileMapper(NICKNAME_FILEPATH_FMT));

        registry.register(StreetNameDatasetKey.class, new StreetNameFileMapper(STREET_NAME_FILEPATH_FMT));
        registry.register(StreetSuffixDatasetKey.class, new StreetSuffixFileMapper(STREET_SUFFIX_FILEPATH_FMT));
        registry.register(CityStateZipDatasetKey.class, new CityStateZipFileMapper(CITY_STATE_ZIP_FILEPATH_FMT));
        registry.register(AddressTwoDatasetKey.class, new AddressTwoFileMapper(ADDRESSTWO_FILEPATH_FMT));

        registry.register(AreaCodeDatasetKey.class, new AreaCodeFileMapper(AREA_CODE_FILEPATH_FMT));

        registry.register(DomainDatasetKey.class, new DomainFileMapper(DOMAIN_FILEPATH_FMT));
        registry.register(UsernameDatasetKey.class, new UsernameFileMapper(USERNAME_FILEPATH_FMT));

        registry.register(KeywordDatasetKey.class, new KeywordFileMapper(KEYWORD_FILEPATH_FMT));

        return registry;
    }

    @Bean(name="nameDatasetResolverRegistry")
    public DatasetResolverRegistry nameDatasetResolverRegistry(
            FileDatasetMapperRegistry fileDatasetMapperRegistry
    ) {
        ResourceLoaderUtil loader = new ResourceLoaderUtil();

        DatasetSource<FirstNameDatasetKey, FirstNameDatasetResult> firstNameFileSrc =
                new FileDatasetSource<>(loader, new FirstNameFileMapper(FIRST_NAME_FILEPATH_FMT));

        DatasetSource<MiddleNameDatasetKey, MiddleNameDatasetResult> middleNameFileSrc =
                new FileDatasetSource<>(loader, new MiddleNameFileMapper(MIDDLE_NAME_FILEPATH_FMT));

        DatasetSource<LastNameDatasetKey, LastNameDatasetResult> lastNameFileSrc =
                new FileDatasetSource<>(loader, new LastNameFileMapper(LAST_NAME_FILEPATH_FMT));

        DatasetSource<NicknameDatasetKey, NicknameDatasetResult> nicknameFileSrc =
                new FileDatasetSource<>(loader, new NicknameFileMapper(NICKNAME_FILEPATH_FMT));

        NameDatasetResolverRegistry registry = new NameDatasetResolverRegistry(
                new FirstNameDatasetResolver(firstNameFileSrc),
                new MiddleNameDatasetResolver(middleNameFileSrc),
                new LastNameDatasetResolver(lastNameFileSrc),
                new NicknameDatasetResolver(nicknameFileSrc)
        );
        return registry;
    }

    @Bean(name="datasetResolverRegistry")
    public DatasetResolverRegistry datasetResolverRegistry(
            //@Value("${" + ConfigKeys.Datasets.StreetName.DEFAULT_FILE + "}") String streetNameFile
    ) {
        ResourceLoaderUtil loader = new ResourceLoaderUtil();

        //DatasetSource<FirstNameDatasetKey, FirstNameDatasetResult> sqlSource =
        //        new SQLiteDatasetSource<>(
        //            connection,
        //            new FirstNameSQLiteMapper()
        //);

        DatasetSource<FirstNameDatasetKey, FirstNameDatasetResult> firstNameFileSource =
                new FileDatasetSource<>(loader, new FirstNameFileMapper(FIRST_NAME_FILEPATH_FMT));
        DatasetSource<MiddleNameDatasetKey, MiddleNameDatasetResult> middleNameFileSrc =
                new FileDatasetSource<>(loader, new MiddleNameFileMapper(MIDDLE_NAME_FILEPATH_FMT));
        DatasetSource<LastNameDatasetKey, LastNameDatasetResult> lastNameFileSrc =
                new FileDatasetSource<>(loader, new LastNameFileMapper(LAST_NAME_FILEPATH_FMT));
        DatasetSource<NicknameDatasetKey, NicknameDatasetResult> nicknameFileSrc =
                new FileDatasetSource<>(loader, new NicknameFileMapper(NICKNAME_FILEPATH_FMT));

        FirstNameDatasetResolver firstNameResolver = new FirstNameDatasetResolver(firstNameFileSource);
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

        DatasetSource<UsernameDatasetKey, UsernameDatasetResult> usernameFileSource =
                new FileDatasetSource<>(loader, new UsernameFileMapper(USERNAME_FILEPATH_FMT));
        DatasetSource<DomainDatasetKey, DomainDatasetResult> domainFileSource =
                new FileDatasetSource<>(loader, new DomainFileMapper(DOMAIN_FILEPATH_FMT));

        DatasetSource<AreaCodeDatasetKey, AreaCodeDatasetResult> areaCodeFileSource =
                new FileDatasetSource<>(loader, new AreaCodeFileMapper(AREA_CODE_FILEPATH_FMT));
        DatasetSource<KeywordDatasetKey, KeywordDatasetResult> keywordFileSource =
                new FileDatasetSource<>(loader, new KeywordFileMapper(KEYWORD_FILEPATH_FMT));

        StreetNameDatasetResolver streetNameResolver = new StreetNameDatasetResolver(streetNameFileSource);
        StreetSuffixDatasetResolver streetSuffixResolver = new StreetSuffixDatasetResolver(streetSuffixFileSource);
        AddressTwoDatasetResolver addressTwoResolver = new AddressTwoDatasetResolver(addressTwoFileSource);
        CityStateZipDatasetResolver cityStateZipResolver = new CityStateZipDatasetResolver(cityStateZipFileSource);

        UsernameDatasetResolver usernameResolver = new UsernameDatasetResolver(usernameFileSource);
        DomainDatasetResolver domainResolver = new DomainDatasetResolver(domainFileSource);

        AreaCodeDatasetResolver areaCodeResolver = new AreaCodeDatasetResolver(areaCodeFileSource);

        KeywordDatasetResolver keywordResolver = new KeywordDatasetResolver(keywordFileSource);

        return new DatasetResolverRegistry(
                firstNameResolver, 
                middleNameResolver, 
                lastNameResolver, 
                nicknameResolver,
                streetNameResolver, 
                streetSuffixResolver,
                addressTwoResolver, 
                cityStateZipResolver, 
                areaCodeResolver,
                usernameResolver, 
                domainResolver,
                keywordResolver
        );
    }
}
