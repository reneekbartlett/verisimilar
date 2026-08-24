package com.reneekbartlett.verisimilar.core.datasets;

import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;

public interface DatasetSource<K, R> {
    R load(K key);
    ResourceLoaderUtil getLoader();
}
