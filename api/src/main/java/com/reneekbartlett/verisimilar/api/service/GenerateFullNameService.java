package com.reneekbartlett.verisimilar.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.reneekbartlett.verisimilar.core.generator.FullNameGenerator;
import com.reneekbartlett.verisimilar.core.model.FullName;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

@Service
public class GenerateFullNameService {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateFullNameService.class);

    private final FullNameGenerator fullNameGenerator;

    public GenerateFullNameService(FullNameGenerator fullNameGenerator) {
        this.fullNameGenerator = fullNameGenerator;
    }

    public String generate() {
        return generate(SelectionFilter.empty());
    }

    public String generate(SelectionFilter filter) {
        FullName fullName = fullNameGenerator.generate(filter);
        return fullName.toString();
    }
}
