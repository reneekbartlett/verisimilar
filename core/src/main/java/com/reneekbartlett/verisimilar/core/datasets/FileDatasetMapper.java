package com.reneekbartlett.verisimilar.core.datasets;

import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;

public interface FileDatasetMapper<K, R> {
    R loadFromFile(K key, ResourceLoaderUtil loader);
}
