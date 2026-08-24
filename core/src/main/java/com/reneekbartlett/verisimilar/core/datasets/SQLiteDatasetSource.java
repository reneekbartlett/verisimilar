package com.reneekbartlett.verisimilar.core.datasets;

import java.sql.Connection;

import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;

public class SQLiteDatasetSource<K, R> implements DatasetSource<K, R> {

    private final Connection connection;
    private final SQLiteDatasetMapper<K, R> mapper;

    public SQLiteDatasetSource(Connection connection, SQLiteDatasetMapper<K, R> mapper) {
        this.connection = connection;
        this.mapper = mapper;
    }

    @Override
    public R load(K key) {
        return mapper.loadFromDatabase(key, connection);
    }

    @Override
    public ResourceLoaderUtil getLoader() {
        // TODO Auto-generated method stub
        return null;
    }
}
