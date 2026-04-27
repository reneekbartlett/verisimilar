package com.reneekbartlett.verisimilar.core.datasets.resolver;

/***
 * 
 * @param <K> DatasetKey
 * @param <R> DatasetResult
 */
public interface DatasetResolver<K, R> {

    /**
     * The key type this resolver supports.
     */
    Class<K> keyType();

    Class<R> resultType();

    /**
     * Resolve the dataset for the given key.
     * DatasetResult resolve(DatasetKey key);
     */
    R resolve(K key);

}
