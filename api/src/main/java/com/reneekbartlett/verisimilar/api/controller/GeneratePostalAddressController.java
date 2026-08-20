package com.reneekbartlett.verisimilar.api.controller;

import com.reneekbartlett.verisimilar.api.service.GeneratePostalAddressService;
import com.reneekbartlett.verisimilar.api.shared.annotation.RateLimited;
import com.reneekbartlett.verisimilar.api.model.GeneratorFilter;
import com.reneekbartlett.verisimilar.core.model.USRegion;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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

    public GeneratePostalAddressController(GeneratePostalAddressService generateService) {
        this.generateService = generateService;
    }

    // TODO: Add Converters for UnitType and AddressCategory?
    @RateLimited(capacity = 300, durationSeconds = 60)
    @Operation(summary = "Generate postal address", description = "Retrieves 1 generated postal address.")
    @GetMapping
    public ResponseEntity<Object> generate(
            GeneratorFilter filters,
            @RequestParam(name="CITY", required=false) String city,
            @RequestParam(name="STATE", required=false) USState state,
            @RequestParam(name="ZIP_CODE", required=false) String zipCode,
            @RequestParam(name="REGION", required=false) USRegion region,
            @RequestParam(name="STREET_NAME", required=false) String streetName,
            @RequestParam(name="STREET_SUFFIX", required=false) String streetSuffix,
            @RequestParam(name="UNIT_TYPE", required=false) String unitType,
            @RequestParam(name="ADDRESS_CATEGORY", required=false) String addressCategory,
            @RequestParam(name="AREA_CODE", required=false) String areaCode
    ) {
        SelectionFilter.Builder filterBuilder;
        if (filters != null) {
            filterBuilder = filters.getSelectionFilterBuilder();
        } else {
            filterBuilder = SelectionFilter.builder();
        }

        // TODO
        if(city != null) filterBuilder.city(city);
        if(state != null) filterBuilder.state(state);
        if(zipCode != null) filterBuilder.zipCode(zipCode);
        if(region != null) filterBuilder.region(region);
        if(streetName != null) filterBuilder.streetName(streetName);
        if(streetSuffix != null) filterBuilder.streetSuffix(streetSuffix);
        //if(unitType != null) filterBuilder.unitType(unitType);
        //if(addressCategory != null) filterBuilder.addressCategory(addressCategory);
        if(areaCode != null) filterBuilder.areaCode(areaCode);

        var postalAddress = generateService.generate(filterBuilder.build());

        return ResponseEntity.ok().body(postalAddress);
    }
}
