package com.reneekbartlett.verisimilar.api.service;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

@Component
public class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {

    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter<>(targetType);
    }

    private static class StringToEnumConverter<T extends Enum> implements Converter<String, T> {
        private final Class<T> enumType;

        public StringToEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            if (source.isEmpty()) {
                return null;
            }
            try {
                // Convert incoming string to uppercase to match standard Enum declaration
                return (T) Enum.valueOf(this.enumType, source.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                // Optional fallback: handle alternate naming or return null safely
                throw new IllegalArgumentException("Unknown enum value: " + source);
            }
        }
    }
}
