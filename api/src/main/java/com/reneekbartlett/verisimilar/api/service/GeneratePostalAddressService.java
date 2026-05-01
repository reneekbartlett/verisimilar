package com.reneekbartlett.verisimilar.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.reneekbartlett.verisimilar.core.generator.PostalAddressRecordGenerator;
import com.reneekbartlett.verisimilar.core.model.PostalAddress;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

@Service
public class GeneratePostalAddressService {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratePostalAddressService.class);

    private final PostalAddressRecordGenerator postalAddressGenerator;

    public GeneratePostalAddressService(PostalAddressRecordGenerator postalAddressGenerator) {
        this.postalAddressGenerator = postalAddressGenerator;
    }

    public String generate() {
        return generate(SelectionFilter.empty());
    }

    public String generate(SelectionFilter filter) {
        PostalAddress postalAddress = postalAddressGenerator.generate(filter);
        return postalAddress.toString();
    }
}
