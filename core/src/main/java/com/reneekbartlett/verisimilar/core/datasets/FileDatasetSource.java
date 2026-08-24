package com.reneekbartlett.verisimilar.core.datasets;

import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;

public class FileDatasetSource<K, R> implements DatasetSource<K, R> {

    private final ResourceLoaderUtil loader;
    private final FileDatasetMapper<K, R> mapper;

    public FileDatasetSource(ResourceLoaderUtil loader, FileDatasetMapper<K, R> mapper) {
        this.loader = loader;
        this.mapper = mapper;
    }

    @Override
    public R load(K key) {
        // Delegates to a mapper that knows how to turn a key into a file path
        // and how to interpret the file contents.
        return mapper.loadFromFile(key, loader);
    }

    @Override
    public ResourceLoaderUtil getLoader() {
        return this.loader;
    }

    public Map<String, Double> loadWeightedMap(String resourcePath) {
        return loader.loadWeightedMap(resourcePath);
    }
}
