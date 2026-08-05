package com.reneekbartlett.verisimilar.api.controller;

import com.reneekbartlett.verisimilar.api.service.GeneratePostalAddressService;
import com.reneekbartlett.verisimilar.api.shared.annotation.RateLimited;
import com.reneekbartlett.verisimilar.api.util.JsonApiParser;
import com.reneekbartlett.verisimilar.api.util.JsonApiParser.FilterConditions;
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
@Tag(name = "Generate Postal Address", description = "Generate Postal Address API")
@RequestMapping("/api/generate/postalAddress")
public class GeneratePostalAddressController {

    private final GeneratePostalAddressService generateService;

    private final List<TemplateField> filterFields;
    private final JsonApiParser jsonRequestParser;

    public GeneratePostalAddressController(GeneratePostalAddressService generateService) {
        this.generateService = generateService;
        this.filterFields = List.of(TemplateField.AREA_CODE);
        this.jsonRequestParser = new JsonApiParser(this.filterFields);
    }

    @RateLimited(capacity = 300, durationSeconds = 60)
    @Operation(summary = "Generate postal address", description = "Retrieves 1 generated postal address.")
    @GetMapping
    public ResponseEntity<Object> generate(
            HttpServletRequest request,
            @RequestParam(name="areaCode", required=false) String areaCode
    ) {
        SelectionFilter.Builder filterBuilder;
        FilterConditions filters = jsonRequestParser.parse(request.getParameterMap());
        if (filters.size() > 0) {
            filterBuilder = filters.toSelectionFilterBuilder();
        } else {
            filterBuilder = SelectionFilter.builder();
        }

        var postalAddress = generateService.generate(filterBuilder.build());

        return ResponseEntity.ok().body(postalAddress);
    }
}
