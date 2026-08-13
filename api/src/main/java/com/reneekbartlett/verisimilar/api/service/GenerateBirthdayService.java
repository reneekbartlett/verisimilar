package com.reneekbartlett.verisimilar.api.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.reneekbartlett.verisimilar.core.generator.BirthdayGenerator;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

@Service
public class GenerateBirthdayService {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateBirthdayService.class);

    private final BirthdayGenerator birthdayGenerator;

    public GenerateBirthdayService(BirthdayGenerator birthdayGenerator) {
        this.birthdayGenerator = birthdayGenerator;
    }

    public String generate() {
        return generate(SelectionFilter.empty());
    }

    public String generate(SelectionFilter filter) {
        LocalDate birthday = birthdayGenerator.generate(filter);
        return birthday.toString();
    }
}
