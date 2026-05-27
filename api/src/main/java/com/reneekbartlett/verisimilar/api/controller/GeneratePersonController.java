package com.reneekbartlett.verisimilar.api.controller;

import com.reneekbartlett.verisimilar.api.dto.PersonResponseDto;
import com.reneekbartlett.verisimilar.api.service.GeneratePersonService;
import com.reneekbartlett.verisimilar.api.util.JsonApiParser;
import com.reneekbartlett.verisimilar.api.util.JsonApiParser.FilterConditions;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import jakarta.servlet.http.HttpServletRequest;

import java.util.EnumSet;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generate/person")
public class GeneratePersonController {

    private final GeneratePersonService generateService;

    public GeneratePersonController(GeneratePersonService generateService) {
        this.generateService = generateService;
    }

    @GetMapping
    public ResponseEntity<PersonResponseDto> generate(
            @RequestParam(name="gender", required=false) String gender,
            @RequestParam(name="state", required=false) String state,
            HttpServletRequest request
    ) {
        SelectionFilter.Builder filterBuilder;
        FilterConditions filters = JsonApiParser.parse(request.getParameterMap());
        if (filters.size() > 0) {
            filterBuilder = filters.toSelectionFilterBuilder();
        } else {
            filterBuilder = SelectionFilter.builder();
        }

        if(GenderIdentity.fromText(gender) != null) filterBuilder.gender(GenderIdentity.fromText(gender));
        if(USState.fromAbbreviation(state) != null) filterBuilder.states(EnumSet.of(USState.fromAbbreviation(state)));

        PersonResponseDto person = generateService.generate(filterBuilder.build());

        return ResponseEntity.ok().body(person);
    }
}
