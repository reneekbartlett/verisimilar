package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class DatasetCache<K, R> {

    //private static final Logger LOGGER = LoggerFactory.getLogger(DatasetCache.class);

    private final Map<K, R> cache = new ConcurrentHashMap<>();

    /***
     * 
     * @param key
     * @param loader
     * @return
     */
    public R getOrLoad(K key, Function<K, R> loader) {
        //LOGGER.debug("getOrLoad - key: {}; cached.size():{}", key, cache.size());
        //LOGGER.debug("containsKey:{}; key:{}", cache.containsKey(key), key);
        return cache.computeIfAbsent(key, loader);
    }

    public boolean isLoaded(K key) {
        return cache.containsKey(key);
    }
}
