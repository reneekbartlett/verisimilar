package com.reneekbartlett.verisimilar.api.controller;

import com.reneekbartlett.verisimilar.api.service.GenerateEmailAddressService;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generate/email")
public class GenerateEmailAddressController {

    private final GenerateEmailAddressService generateService;

    public GenerateEmailAddressController(GenerateEmailAddressService generateService) {
        this.generateService = generateService;
    }

    @GetMapping
    public ResponseEntity<Object> generate(
            @RequestParam(name="gender", required=false) String gender,
            @RequestParam(name="fname", required=false) String fname,
            @RequestParam(name="mname", required=false) String mname,
            @RequestParam(name="lname", required=false) String lname,
            @RequestParam(name="birthday", required=false) String birthday,
            @RequestParam(name="domain", required=false) String domain,
            @RequestParam(name="domain_type", required=false) String domain_type,
            @RequestParam(name="username", required=false) String username,
            @RequestParam(name="username_type", required=false) String username_type
    ) {
        // TODO:  Validate values.  Empty/null handling.
        SelectionFilter.Builder filter = SelectionFilter.builder();
        if(GenderIdentity.fromText(gender) != null) filter.gender(GenderIdentity.fromText(gender));
        if(fname != null) filter.firstName(fname);
        if(mname != null) filter.middleName(mname);
        if(lname != null) filter.lastName(lname);

        var email = generateService.generate(filter.build());

        return ResponseEntity.ok().body(email);
    }
}
