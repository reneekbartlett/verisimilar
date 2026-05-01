package com.reneekbartlett.verisimilar.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.reneekbartlett.verisimilar.api.dto.PersonResponseDto;
import com.reneekbartlett.verisimilar.core.generator.AsyncPersonGenerator;
//import com.reneekbartlett.verisimilar.core.generator.PersonGenerator;
import com.reneekbartlett.verisimilar.core.model.PersonRecord;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

@Service
public class GeneratePersonService {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratePersonService.class);

    //private final JsonMapper jsonMapper;
    //private final PersonGenerator personGenerator;
    private final AsyncPersonGenerator asyncPersonGenerator;

    public GeneratePersonService(AsyncPersonGenerator asyncPersonGenerator) {
        this.asyncPersonGenerator = asyncPersonGenerator;
    }

    public PersonResponseDto generate() {
        return generate(SelectionFilter.empty());
    }

    public PersonResponseDto generate(SelectionFilter filter) {
        PersonRecord person = asyncPersonGenerator.generate(filter);
        return new PersonResponseDto(person.firstName(), person.middleName(), person.lastName(), 
                person.birthday(),
                person.gender(),
                person.address1(), // String address1,
                person.address2(), // String address2,
                person.city(), // String city,
                person.state(), // String state,
                person.zip(), // String zip,
                person.emailAddress().email(), // String emailAddress,
                person.phoneNumber().toString() // String phoneNumber
        );
    }
}
