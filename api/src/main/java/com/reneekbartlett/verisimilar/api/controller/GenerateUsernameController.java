package com.reneekbartlett.verisimilar.api.controller;

import com.reneekbartlett.verisimilar.api.service.GenerateUsernameService;
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
@RequestMapping("/api/generate/username")
public class GenerateUsernameController {

    private final GenerateUsernameService generateService;
    private final List<TemplateField> filterFields;
    private final JsonApiParser jsonRequestParser;

    public GenerateUsernameController(GenerateUsernameService generateService) {
        this.generateService = generateService;
        this.filterFields = List.of();
        this.jsonRequestParser = new JsonApiParser(this.filterFields);
    }

    @GetMapping
    public ResponseEntity<Object> generate(
            HttpServletRequest request,
            @RequestParam String first,
            @RequestParam String last,
            @RequestParam(defaultValue = "us") String region
    ) {
        SelectionFilter.Builder filterBuilder;
        FilterConditions filters = jsonRequestParser.parse(request.getParameterMap());
        if (filters.size() > 0) {
            filterBuilder = filters.toSelectionFilterBuilder();
        } else {
            filterBuilder = SelectionFilter.builder();
        }

        // TODO: include filter
        //String username = generateService.generateUsername(first, last, region);
        String username = generateService.generate(filterBuilder.build());

        return ResponseEntity.ok().body(username);
    }
}
