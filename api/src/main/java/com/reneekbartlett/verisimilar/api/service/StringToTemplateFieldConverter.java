package com.reneekbartlett.verisimilar.api.service;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.reneekbartlett.verisimilar.core.model.TemplateField;

@Component
public class StringToTemplateFieldConverter implements Converter<String, TemplateField> {
    @Override
    public TemplateField convert(String source) {
        return TemplateField.fromValue(source);
    }
}
