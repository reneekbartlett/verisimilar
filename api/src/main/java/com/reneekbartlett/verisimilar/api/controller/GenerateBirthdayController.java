package com.reneekbartlett.verisimilar.api.controller;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reneekbartlett.verisimilar.api.model.GeneratorFilter;
import com.reneekbartlett.verisimilar.api.service.GenerateBirthdayService;
import com.reneekbartlett.verisimilar.api.shared.annotation.RateLimited;
import com.reneekbartlett.verisimilar.core.model.Generation;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Generate Birthday", description = "Generate Birthday API")
@RequestMapping("/api/generate")
public class GenerateBirthdayController {

    private final GenerateBirthdayService generateService;

    public GenerateBirthdayController(GenerateBirthdayService generateService) {
        this.generateService = generateService;
    }

    @RateLimited(capacity = 300, durationSeconds = 60)
    @Operation(summary = "Generate Birthday", description = "Retrieves 1 generated birthday.")
    @GetMapping("/birthday")
    public ResponseEntity<Object> generate(
            GeneratorFilter filters,
            @RequestParam(name="BIRTH_YEAR", required=false) String birthYear,
            @RequestParam(name="BIRTH_MONTH", required=false) String birthMonth,
            @RequestParam(name="BIRTH_DAY", required=false) String birthDayOfMonth,
            @RequestParam(name="BIRTH_SIGN", required=false) String birthSign,
            @RequestParam(name="GENERATION", required=false) Generation generation
    ) {
        SelectionFilter.Builder filterBuilder;
        if (filters != null) {
            filterBuilder = filters.getSelectionFilterBuilder();
        } else {
            filterBuilder = SelectionFilter.builder();
        }

        if(generation != null) filterBuilder.generation(generation);

        // TODO
        if(birthYear != null) {
            filterBuilder.minYear(NumberUtils.toInt(birthYear));
            filterBuilder.maxYear(NumberUtils.toInt(birthYear));
        }

        //if(birthSign != null) {
        //    AstrologySign astrologySign = AstrologySign.fromName(birthSign);
        //}

        var birthday = generateService.generate(filterBuilder.build());

        return ResponseEntity.ok().body(birthday);
    }
}
