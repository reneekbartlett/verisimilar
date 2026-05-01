package com.reneekbartlett.verisimilar.api.controller;

import com.reneekbartlett.verisimilar.api.service.GeneratePostalAddressService;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generate/postalAddress")
public class GeneratePostalAddressController {

    private final GeneratePostalAddressService generateService;

    public GeneratePostalAddressController(GeneratePostalAddressService generateService) {
        this.generateService = generateService;
    }

    @GetMapping
    public ResponseEntity<Object> generate(
            @RequestParam(name="gender", required=false) String gender,
            @RequestParam(name="birthday", required=false) String birthday
    ) {
        // TODO:  Validate values.  Empty/null handling.
        SelectionFilter.Builder filter = SelectionFilter.builder();
        if(GenderIdentity.fromText(gender) != null) filter.gender(GenderIdentity.fromText(gender));

        var postalAddress = generateService.generate(filter.build());

        return ResponseEntity.ok().body(postalAddress);
    }
}
