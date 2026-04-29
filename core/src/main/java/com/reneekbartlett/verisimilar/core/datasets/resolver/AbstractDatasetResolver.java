package com.reneekbartlett.verisimilar.core.datasets.resolver;

import com.reneekbartlett.verisimilar.core.datasets.key.DatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.DatasetResult;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDatasetResolver<K, R> implements DatasetResolver<K, R> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractDatasetResolver.class);

    private final DatasetCache<K, R> datasetCache = new DatasetCache<>();

    protected final ResourceLoaderUtil loader;

    protected AbstractDatasetResolver(ResourceLoaderUtil loader) {
        this.loader = loader;
    }

    @Override
    public abstract Class<K> keyType();

    @Override
    public R resolve(K key) {
        //LOGGER.debug("resolve - key:{}; isLoaded:{}", key, datasetCache.isLoaded(key));
        return datasetCache.getOrLoad(key, this::loadForKey);
    }

    protected DatasetResult loadForKey(DatasetKey key) {
        return null;
    }

    protected abstract R loadForKey(K key);

    /**
     * Convenience method for loading a weighted map from a CSV.
     */
    protected Map<String, Double> load(String path) {
        return loader.loadWeightedMap(path);
    }

    /**
     * Convenience method for checking dataset existence.
     */
    protected boolean exists(String path) {
        return loader.exists(path);
    }
}
