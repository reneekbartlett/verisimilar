package com.reneekbartlett.verisimilar.api.controller;

import com.reneekbartlett.verisimilar.api.service.GenerateUsernameService;
import com.reneekbartlett.verisimilar.api.model.GeneratorFilter;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Generate Username", description = "Generate Username API")
@RequestMapping("/api/generate/username")
public class GenerateUsernameController {

    private final GenerateUsernameService generateService;

    public GenerateUsernameController(GenerateUsernameService generateService) {
        this.generateService = generateService;
    }

    @Operation(summary = "Generate Username", description = "Retrieves 1 generated username.")
    @GetMapping
    public ResponseEntity<Object> generate(
            GeneratorFilter filters,
            HttpServletRequest request,
            @RequestParam(name="FIRST_NAME", required=false) String firstName,
            @RequestParam(name="MIDDLE_NAME", required=false) String middleName,
            @RequestParam(name="LAST_NAME", required=false) String lastName,
            @RequestParam(name="USERNAME_TYPE", required=false) String usernameType,
            @RequestParam(name="DOMAIN_TYPE", required=false) String domainType,
            @RequestParam(name="BIRTHDAY", required=false) String birthday
    ) {
        SelectionFilter.Builder filterBuilder;
        if (filters != null) {
            filterBuilder = filters.getSelectionFilterBuilder();
        } else {
            filterBuilder = SelectionFilter.builder();
        }

        if(firstName != null) filterBuilder.firstName(firstName);
        if(middleName != null) filterBuilder.middleName(middleName);
        if(lastName != null) filterBuilder.lastName(lastName);

        //if(usernameType != null) filterBuilder.usernameType(usernameType);
        //if(domainType != null) filterBuilder.domainType(domainType);
        //if(birthday != null) filterBuilder.birthday(birthday);

        String username = generateService.generate(filterBuilder.build());

        return ResponseEntity.ok().body(username);
    }
}
