package com.reneekbartlett.verisimilar.api.service;

import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.reneekbartlett.verisimilar.core.model.Ethnicity;

@Component
public class StringToEthnicityConverter implements Converter<String, Ethnicity> {
    @Override
    public @Nullable Ethnicity convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }
        return Ethnicity.fromText(source);
    }
}
