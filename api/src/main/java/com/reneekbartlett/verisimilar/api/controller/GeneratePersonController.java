package com.reneekbartlett.verisimilar.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reneekbartlett.verisimilar.api.dto.PersonResponseDto;
import com.reneekbartlett.verisimilar.api.model.GeneratorFilter;
import com.reneekbartlett.verisimilar.api.service.GeneratePersonService;
import com.reneekbartlett.verisimilar.api.shared.annotation.RateLimited;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.Generation;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/generate")
@Tag(name = "Generate Person", description = "Generate Person API")
public class GeneratePersonController {

    private final GeneratePersonService generateService;

    public GeneratePersonController(GeneratePersonService generateService) {
        this.generateService = generateService;
    }

    // Limited to 5 requests per second (aka 300 requests per 60 seconds)
    @RateLimited(capacity = 300, durationSeconds = 60)
    @Operation(summary = "Generate person", description = "Retrieves 1 generated person.")
    @GetMapping("person")
    public ResponseEntity<PersonResponseDto> generate(
            GeneratorFilter filters,
            @RequestParam(name="GENDER_IDENTITY", required=false) GenderIdentity gender,
            @RequestParam(name="FIRST_NAME", required=false) String firstName,
            @RequestParam(name="MIDDLE_NAME", required=false) String middleName,
            @RequestParam(name="LAST_NAME", required=false) String lastName,
            @RequestParam(name="BIRTHDAY", required=false) String birthday,
            @RequestParam(name="ETHNICITY", required=false) Ethnicity ethnicity,
            @RequestParam(name="GENERATION", required=false) Generation generation,
            @RequestParam(name="CITY", required=false) String city,
            @RequestParam(name="STATE", required=false) String state
    ) {
        SelectionFilter.Builder filterBuilder;
        if (filters != null) {
            filterBuilder = filters.getSelectionFilterBuilder();
        } else {
            filterBuilder = SelectionFilter.builder();
        }

        // TODO:  TemplateField.DOMAIN, TemplateField.DOMAIN_TYPE, 
        // TODO:  TemplateField.USERNAME, TemplateField.USERNAME_TYPE
        if(gender != null) filterBuilder.gender(gender);
        if(firstName != null) filterBuilder.firstName(firstName);
        if(middleName != null) filterBuilder.middleName(middleName);
        if(lastName != null) filterBuilder.lastName(lastName);
        if(birthday != null) filterBuilder.birthday(birthday);
        if(ethnicity != null) filterBuilder.ethnicity(ethnicity);
        if(generation != null) filterBuilder.generation(generation);

        //if(USState.fromAbbreviation(state) != null) 
        //    filterBuilder.states(EnumSet.of(USState.fromAbbreviation(state)));

        PersonResponseDto person = generateService.generate(filterBuilder.build());

        return ResponseEntity.ok().body(person);
    }

}
