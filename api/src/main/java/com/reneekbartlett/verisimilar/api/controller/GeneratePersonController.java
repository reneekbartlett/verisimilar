package com.reneekbartlett.verisimilar.api.controller;

import com.reneekbartlett.verisimilar.api.dto.PersonResponseDto;
import com.reneekbartlett.verisimilar.api.service.GeneratePersonService;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import java.util.Set;

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
    public ResponseEntity<Object> generate(
            @RequestParam(name="gender", required=false) String gender,
            @RequestParam(name="state", required=false) String state
    ) {
        SelectionFilter.Builder filter = SelectionFilter.builder();
        if(GenderIdentity.fromText(gender) != null) filter.gender(GenderIdentity.fromText(gender));
        if(USState.fromAbbreviation(state) != null) filter.states(Set.of(USState.fromAbbreviation(state)));

        PersonResponseDto person = generateService.generate(filter.build());

        return ResponseEntity.ok()
                .body(person.toString());
    }
}
