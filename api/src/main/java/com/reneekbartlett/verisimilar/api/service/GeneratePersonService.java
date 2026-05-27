package com.reneekbartlett.verisimilar.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.reneekbartlett.verisimilar.api.dto.PersonResponseDto;
import com.reneekbartlett.verisimilar.core.generator.AsyncPersonGenerator;
import com.reneekbartlett.verisimilar.core.model.PersonRecord;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

@Service
public class GeneratePersonService {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratePersonService.class);

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
                person.address1(),
                person.address2(),
                person.city(),
                person.state(),
                person.zip(),
                person.emailAddress().email(),
                person.phoneNumber().toString()
        );
    }
}
