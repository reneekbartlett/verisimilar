package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class DatasetCache<K, R> {

    private final Map<K, R> cache = new ConcurrentHashMap<>();

    /***
     * 
     * @param key
     * @param loader
     * @return
     */
    public R getOrLoad(K key, Function<K, R> loader) {
        return cache.computeIfAbsent(key, loader);
    }

    public boolean isLoaded(K key) {
        return cache.containsKey(key);
    }
}
