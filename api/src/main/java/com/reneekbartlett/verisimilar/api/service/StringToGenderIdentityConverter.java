package com.reneekbartlett.verisimilar.api.service;

import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.reneekbartlett.verisimilar.core.model.GenderIdentity;

@Component
public class StringToGenderIdentityConverter implements Converter<String, GenderIdentity> {
    @Override
    public @Nullable GenderIdentity convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }
        return GenderIdentity.valueOf(source.trim().toUpperCase());
    }
}
