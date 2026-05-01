package com.reneekbartlett.verisimilar.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.reneekbartlett.verisimilar.core.generator.PhoneNumberGenerator;
import com.reneekbartlett.verisimilar.core.model.PhoneNumber;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

@Service
public class GeneratePhoneNumberService {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratePhoneNumberService.class);

    private final PhoneNumberGenerator phoneNumberGenerator;

    public GeneratePhoneNumberService(PhoneNumberGenerator phoneNumberGenerator) {
        this.phoneNumberGenerator = phoneNumberGenerator;
    }

    public String generate() {
        return generate(SelectionFilter.empty());
    }

    public String generate(SelectionFilter filter) {
        PhoneNumber phoneNumber = phoneNumberGenerator.generate(filter);
        return phoneNumber.toString();
    }
}
