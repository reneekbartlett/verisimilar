package com.reneekbartlett.verisimilar.api.controller;

import com.reneekbartlett.verisimilar.api.service.GeneratePhoneNumberService;
import com.reneekbartlett.verisimilar.api.util.JsonApiParser;
import com.reneekbartlett.verisimilar.api.util.JsonApiParser.FilterConditions;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generate/phoneNumber")
public class GeneratePhoneNumberController {

    private final GeneratePhoneNumberService generateService;

    private final List<TemplateField> filterFields;
    private final JsonApiParser jsonRequestParser;

    public GeneratePhoneNumberController(GeneratePhoneNumberService generateService) {
        this.generateService = generateService;
        this.filterFields = List.of(TemplateField.AREA_CODE);
        this.jsonRequestParser = new JsonApiParser(this.filterFields);
    }

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

        if(areaCode != null) filterBuilder.areaCode(areaCode);

        var phoneNumber = generateService.generate(filterBuilder.build());

        return ResponseEntity.ok().body(phoneNumber);
    }
}
