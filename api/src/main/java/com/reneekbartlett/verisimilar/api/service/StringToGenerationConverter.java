package com.reneekbartlett.verisimilar.api.service;

import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.reneekbartlett.verisimilar.core.model.Generation;

@Component
public class StringToGenerationConverter implements Converter<String, Generation> {
    @Override
    public @Nullable Generation convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }
        return Generation.fromText(source);
    }
}
