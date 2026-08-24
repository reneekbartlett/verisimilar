package com.reneekbartlett.verisimilar.api.service;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.reneekbartlett.verisimilar.core.model.FilterOperator;

@Component
public class StringToFilterOperatorConverter implements Converter<String, FilterOperator> {
    @Override
    public FilterOperator convert(String source) {
        return FilterOperator.fromKeyword(source);
    }
}
