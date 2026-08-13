package com.reneekbartlett.verisimilar.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reneekbartlett.verisimilar.api.model.GeneratorFilter;
import com.reneekbartlett.verisimilar.api.service.GenerateFullNameService;
import com.reneekbartlett.verisimilar.api.shared.annotation.RateLimited;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.Generation;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Generate Full Name", description = "Generate Full Name API")
@RequestMapping("/api/generate")
public class GenerateFullNameController {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateFullNameController.class);
    private final GenerateFullNameService generateService;

    public GenerateFullNameController(GenerateFullNameService generateService) {
        this.generateService = generateService;
    }

    @RateLimited(capacity = 300, durationSeconds = 60)
    @Operation(summary = "Generate Full Name", description = "Retrieves 1 generated full name.")
    @GetMapping("/fullName")
    public ResponseEntity<Object> generate(
            GeneratorFilter filters,
            @RequestParam(name="GENDER_IDENTITY", required=false) GenderIdentity gender,
            @RequestParam(name="FIRST_NAME", required=false) String firstName,
            @RequestParam(name="MIDDLE_NAME", required=false) String middleName,
            @RequestParam(name="LAST_NAME", required=false) String lastName,
            @RequestParam(name="BIRTHDAY", required=false) String birthday,
            @RequestParam(name="ETHNICITY", required=false) Ethnicity ethnicity,
            @RequestParam(name="GENERATION", required=false) Generation generation
    ) {
        SelectionFilter.Builder filterBuilder;
        if (filters != null) {
            filterBuilder = filters.getSelectionFilterBuilder();
        } else {
            filterBuilder = SelectionFilter.builder();
        }

        if(gender != null) filterBuilder.gender(gender);
        if(firstName != null) filterBuilder.firstName(firstName);
        if(middleName != null) filterBuilder.middleName(middleName);
        if(lastName != null) filterBuilder.lastName(lastName);
        if(birthday != null) filterBuilder.birthday(birthday);
        if(ethnicity != null) filterBuilder.ethnicity(ethnicity);
        if(generation != null) filterBuilder.generation(generation);

        SelectionFilter selectionFilter = filterBuilder.build();
        //LOGGER.debug("{}", selectionFilter);
        var fullName = generateService.generate(selectionFilter);
        //LOGGER.debug("{}", selectionFilter);

        return ResponseEntity.ok().body(fullName);
    }

}
