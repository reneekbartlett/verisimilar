package com.reneekbartlett.verisimilar.core.datasets;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;

public abstract class AbstractFileDatasetMapper<K, R> implements FileDatasetMapper<K, R> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileDatasetMapper.class);

    protected AbstractFileDatasetMapper() {
        //
    }

    /**
    * Each dataset type defines how to build its path.
    */
    protected abstract String resolvePath(K key);

    /**
    * Shared helper for full path resolution.
    */
    protected String fullPath(K key) {
        return "datasets/" + resolvePath(key);
    }

    /**
     * Convenience method for loading a weighted map from a CSV.
     */
    public Map<String, Double> load(ResourceLoaderUtil loader, String path) {
        return loader.loadWeightedMap(path);
    }

    /**
     * Convenience method for checking dataset existence.
     */
    protected boolean exists(ResourceLoaderUtil loader, String path) {
        return loader.exists(path);
    }

}
