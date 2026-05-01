package com.reneekbartlett.verisimilar.api.controller;

import com.reneekbartlett.verisimilar.api.service.GenerateFullNameService;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generate/fullname")
public class GenerateFullNameController {

    private final GenerateFullNameService generateService;

    public GenerateFullNameController(GenerateFullNameService generateService) {
        this.generateService = generateService;
    }

    @GetMapping
    public ResponseEntity<Object> generate(
            @RequestParam(name="gender", required=false) String gender,
            @RequestParam(name="fname", required=false) String fname,
            @RequestParam(name="mname", required=false) String mname,
            @RequestParam(name="lname", required=false) String lname,
            @RequestParam(name="birthday", required=false) String birthday,
            @RequestParam(name="ethnicity", required=false) String ethnicity
    ) {
        // TODO:  Validate values.  Empty/null handling.
        SelectionFilter.Builder filter = SelectionFilter.builder();
        if(GenderIdentity.fromText(gender) != null) filter.gender(GenderIdentity.fromText(gender));
        if(fname != null) filter.firstName(fname);
        if(mname != null) filter.middleName(mname);
        if(lname != null) filter.lastName(lname);
        if(ethnicity != null) filter.ethnicity(Ethnicity.valueOf(ethnicity));

        var fullName = generateService.generate(filter.build());

        return ResponseEntity.ok().body(fullName);
    }
}
