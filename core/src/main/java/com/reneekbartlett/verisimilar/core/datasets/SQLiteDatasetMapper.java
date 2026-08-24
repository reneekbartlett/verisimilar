package com.reneekbartlett.verisimilar.core.datasets;

import java.sql.Connection;

public interface SQLiteDatasetMapper<K, R> {
    R loadFromDatabase(K key, Connection connection);
}
