package com.reneekbartlett.verisimilar.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reneekbartlett.verisimilar.api.model.GeneratorFilter;
import com.reneekbartlett.verisimilar.api.service.GenerateEmailAddressService;
import com.reneekbartlett.verisimilar.api.shared.annotation.RateLimited;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.UsernameType;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Generate Email Address")
@RequestMapping("/api/generate")
public class GenerateEmailAddressController {

    private final GenerateEmailAddressService generateService;

    public GenerateEmailAddressController(GenerateEmailAddressService generateService) {
        this.generateService = generateService;
    }

    @RateLimited(capacity = 300, durationSeconds = 60)
    @Operation(summary = "Generate email address", description = "Retrieves 1 generated email address.")
    @GetMapping("email")
    public ResponseEntity<Object> generate(
            GeneratorFilter filters,
            @RequestParam(name="GENDER_IDENTITY", required=false) GenderIdentity gender,
            @RequestParam(name="FIRST_NAME", required=false) String firstName,
            @RequestParam(name="MIDDLE_NAME", required=false) String middleName,
            @RequestParam(name="LAST_NAME", required=false) String lastName,
            @RequestParam(name="BIRTHDAY", required=false) String birthday,
            @RequestParam(name="DOMAIN", required=false) String domain,
            @RequestParam(name="DOMAIN_TYPE", required=false) String domainType,
            @RequestParam(name="USERNAME", required=false) String username,
            @RequestParam(name="USERNAME_TYPE", required=false) String usernameType
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
        if(domain != null) filterBuilder.domain(domain);
        if(domainType != null) filterBuilder.domainType(DomainType.valueOf(domainType));

        if(username != null) filterBuilder.username(username);
        if(usernameType != null) filterBuilder.usernameType(UsernameType.valueOf(usernameType));

        var email = generateService.generate(filterBuilder.build());

        return ResponseEntity.ok().body(email);
    }
}
