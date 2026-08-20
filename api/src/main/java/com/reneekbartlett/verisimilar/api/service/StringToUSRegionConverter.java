package com.reneekbartlett.verisimilar.api.service;

import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.reneekbartlett.verisimilar.core.model.USRegion;

@Component
public class StringToUSRegionConverter implements Converter<String, USRegion> {
    @Override
    public @Nullable USRegion convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }
        return USRegion.fromText(source);
    }
}
