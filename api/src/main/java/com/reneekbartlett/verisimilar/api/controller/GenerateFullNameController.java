package com.reneekbartlett.verisimilar.api.controller;

import com.reneekbartlett.verisimilar.api.service.GenerateFullNameService;
import com.reneekbartlett.verisimilar.api.shared.annotation.RateLimited;
import com.reneekbartlett.verisimilar.api.util.JsonApiParser;
import com.reneekbartlett.verisimilar.api.util.JsonApiParser.FilterConditions;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Generate Full Name", description = "Generate Full Name API")
@RequestMapping("/api/generate")
public class GenerateFullNameController {

    private final GenerateFullNameService generateService;

    private final List<TemplateField> filterFields;
    private final JsonApiParser jsonRequestParser;

    public GenerateFullNameController(GenerateFullNameService generateService) {
        this.generateService = generateService;
        this.filterFields = List.of(TemplateField.FIRST_NAME, TemplateField.MIDDLE_NAME, TemplateField.LAST_NAME,
                TemplateField.ETHNICITY, TemplateField.BIRTHDAY);
        this.jsonRequestParser = new JsonApiParser(this.filterFields);
    }

    @RateLimited(capacity = 300, durationSeconds = 60)
    @Operation(summary = "Generate Full Name", description = "Retrieves 1 generated full name.")
    @GetMapping("fullName")
    public ResponseEntity<Object> generate(
            @RequestParam(name="gender", required=false) String gender,
            @RequestParam(name="fname", required=false) String fname,
            @RequestParam(name="mname", required=false) String mname,
            @RequestParam(name="lname", required=false) String lname,
            @RequestParam(name="birthday", required=false) String birthday,
            @RequestParam(name="ethnicity", required=false) String ethnicity,
            HttpServletRequest request
    ) {
        // TODO:  Validate values.  Empty/null handling.
        SelectionFilter.Builder filterBuilder;
        FilterConditions filters = jsonRequestParser.parse(request.getParameterMap());
        if (filters.size() > 0) {
            filterBuilder = filters.toSelectionFilterBuilder();
        } else {
            filterBuilder = SelectionFilter.builder();
        }

        if(GenderIdentity.fromText(gender) != null) filterBuilder.gender(GenderIdentity.fromText(gender));
        if(fname != null) filterBuilder.firstName(fname);
        if(mname != null) filterBuilder.middleName(mname);
        if(lname != null) filterBuilder.lastName(lname);
        if(ethnicity != null) filterBuilder.ethnicity(Ethnicity.valueOf(ethnicity));

        var fullName = generateService.generate(filterBuilder.build());

        return ResponseEntity.ok().body(fullName);
    }
}
