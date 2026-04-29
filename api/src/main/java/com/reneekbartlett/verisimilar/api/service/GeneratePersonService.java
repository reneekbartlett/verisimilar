package com.reneekbartlett.verisimilar.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.reneekbartlett.verisimilar.api.dto.PersonResponseDto;
import com.reneekbartlett.verisimilar.core.generator.PersonGenerator;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.PersonRecord;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import tools.jackson.databind.json.JsonMapper;

@Service
public class GeneratePersonService {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratePersonService.class);

    @SuppressWarnings("unused")
    private final JsonMapper jsonMapper;

    private final PersonGenerator personGenerator;

    public GeneratePersonService(PersonGenerator personGenerator, JsonMapper jsonMapper) {
        this.personGenerator = personGenerator;
        this.jsonMapper = jsonMapper;
    }

    public PersonResponseDto generate() {
        return generate(SelectionFilter.empty());
    }

    public PersonResponseDto generate(SelectionFilter filter) {
        DatasetResolutionContext ctx = DatasetResolutionContext.builder().build();

        PersonRecord person = personGenerator.generate(ctx, filter);

        return new PersonResponseDto(person.firstName(), person.middleName(), person.lastName(), 
                person.birthday(),
                // person.gender(),
                GenderIdentity.GENDER_UNSPECIFIED, person.address1(), // String address1,
                person.address2(), // String address2,
                person.city(), // String city,
                person.state(), // String state,
                person.zip(), // String zip,
                person.emailAddress().email(), // String emailAddress,
                person.phoneNumber().toString() // String phoneNumber
        );
    }
}
