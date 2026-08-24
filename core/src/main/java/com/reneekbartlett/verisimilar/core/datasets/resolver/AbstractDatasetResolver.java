package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.datasets.AddressTwoFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.AreaCodeFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.CityStateZipFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.DatasetSource;
import com.reneekbartlett.verisimilar.core.datasets.DomainFileMapper;
import com.reneekbartlett.verisimilar.core.datasets.FileDatasetMapperRegistry;
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

/***
 * AbstractDatasetResolver<DomainDatasetKey, DomainDatasetResult>
 * @param <K>   DatasetKey
 * @param <R>   DatasetResult
 * 
 * For loading data from a CSV or TXT file.
 */
public abstract class AbstractDatasetResolver<K, R> implements DatasetResolver<K, R> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractDatasetResolver.class);

    private final DatasetCache<K, R> datasetCache = new DatasetCache<>();

    private final DatasetSource<K, R> source;

    //protected ResourceLoaderUtil loader;

    protected AbstractDatasetResolver(DatasetSource<K, R> source) {
        this.source = source;
        //this.loader = source.getLoader();
    }

    //protected AbstractDatasetResolver(ResourceLoaderUtil loader) {
    //    this.loader = loader;
    //    FileDatasetMapper<K, R> mapper;
    //    try {
    //        mapper = getFileDatasetMapperRegistry().getMapper(keyType());
    //    } catch (IllegalArgumentException e) {
    //        mapper = null;
    //    }
    //   this.source = new FileDatasetSource(loader, mapper);
    //}

    @Override
    public R resolve(K key) {
        //LOGGER.debug("resolve - key:{}; isLoaded:{}", key, datasetCache.isLoaded(key));
        return datasetCache.getOrLoad(key, this::loadForKey);
    }

    @Override
    public abstract Class<K> keyType();

    @Override
    public abstract Class<R> resultType();

    public R loadForKey(K key) {
        return source.load(key);
    }

    /**
     * Convenience method for loading a weighted map from a CSV.
     */
    protected Map<String, Double> load(String path) {
        return source.getLoader().loadWeightedMap(path);
    }

    /**
     * Convenience method for checking dataset existence.
     */
    protected boolean exists(String path) {
        return source.getLoader().exists(path);
    }

    // TODO: TEMPORARY
    public static FileDatasetMapperRegistry getFileDatasetMapperRegistry() {
        FileDatasetMapperRegistry registry = new FileDatasetMapperRegistry();

        registry.register(FirstNameDatasetKey.class, new FirstNameFileMapper());
        registry.register(MiddleNameDatasetKey.class, new MiddleNameFileMapper());
        registry.register(LastNameDatasetKey.class, new LastNameFileMapper());
        registry.register(NicknameDatasetKey.class, new NicknameFileMapper());

        registry.register(StreetNameDatasetKey.class, new StreetNameFileMapper());
        registry.register(StreetSuffixDatasetKey.class, new StreetSuffixFileMapper());
        registry.register(CityStateZipDatasetKey.class, new CityStateZipFileMapper());
        registry.register(AddressTwoDatasetKey.class, new AddressTwoFileMapper());

        registry.register(AreaCodeDatasetKey.class, new AreaCodeFileMapper());

        registry.register(DomainDatasetKey.class, new DomainFileMapper());
        registry.register(UsernameDatasetKey.class, new UsernameFileMapper());

        registry.register(KeywordDatasetKey.class, new KeywordFileMapper());

        return registry;
    }
}
