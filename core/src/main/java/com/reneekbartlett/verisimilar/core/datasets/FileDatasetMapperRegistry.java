package com.reneekbartlett.verisimilar.core.datasets;

import java.util.HashMap;
import java.util.Map;

public class FileDatasetMapperRegistry {

    private final Map<Class<?>, FileDatasetMapper<?, ?>> registry = new HashMap<>();

    public <K, R> void register(Class<K> keyType, FileDatasetMapper<K, R> mapper) {
        registry.put(keyType, mapper);
    }

    @SuppressWarnings("unchecked")
    public <K, R> FileDatasetMapper<K, R> getMapper(Class<K> keyType) {
        FileDatasetMapper<?, ?> mapper = registry.get(keyType);
        if (mapper == null) {
            throw new IllegalArgumentException("No mapper registered for key type: " + keyType.getName());
        }
        return (FileDatasetMapper<K, R>) mapper;
    }
}
