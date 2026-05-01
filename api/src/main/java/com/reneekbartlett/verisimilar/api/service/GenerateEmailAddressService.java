package com.reneekbartlett.verisimilar.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.reneekbartlett.verisimilar.core.generator.EmailAddressGenerator;
import com.reneekbartlett.verisimilar.core.model.EmailAddressRecord;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

@Service
public class GenerateEmailAddressService {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateEmailAddressService.class);

    private final EmailAddressGenerator emailAddressGenerator;

    public GenerateEmailAddressService(EmailAddressGenerator emailAddressGenerator) {
        this.emailAddressGenerator = emailAddressGenerator;
    }

    public String generate() {
        return generate(SelectionFilter.empty());
    }

    public String generate(SelectionFilter filter) {
        EmailAddressRecord emailAddress = emailAddressGenerator.generate(filter);
        return emailAddress.toString();
    }
}
