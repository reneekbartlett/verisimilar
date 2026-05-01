package com.reneekbartlett.verisimilar.api.controller;

import com.reneekbartlett.verisimilar.api.service.GeneratePhoneNumberService;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generate/phoneNumber")
public class GeneratePhoneNumberController {

    private final GeneratePhoneNumberService generateService;

    public GeneratePhoneNumberController(GeneratePhoneNumberService generateService) {
        this.generateService = generateService;
    }

    @GetMapping
    public ResponseEntity<Object> generate(
            @RequestParam(name="areaCode", required=false) String areaCode
    ) {
        // TODO:  Validate values.  Empty/null handling.
        SelectionFilter.Builder filter = SelectionFilter.builder();
        //if(areaCode != null) filter.areaCode(areaCode);

        var phoneNumber = generateService.generate(filter.build());

        return ResponseEntity.ok().body(phoneNumber);
    }
}
