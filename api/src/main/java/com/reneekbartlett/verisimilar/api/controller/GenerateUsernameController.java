package com.reneekbartlett.verisimilar.api.controller;

import com.reneekbartlett.verisimilar.api.service.GenerateUsernameService;
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
@Tag(name = "Generate Username", description = "Generate Username API")
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

    @Operation(summary = "Generate Username", description = "Retrieves 1 generated username.")
    @GetMapping
    public ResponseEntity<Object> generate(
            HttpServletRequest request,
            @RequestParam(name="first", required=false) String first,
            @RequestParam(name="last", required=false) String last,
            @RequestParam(name="region", defaultValue = "us") String region
    ) {
        SelectionFilter.Builder filterBuilder;
        FilterConditions filters = jsonRequestParser.parse(request.getParameterMap());
        if (filters.size() > 0) {
            filterBuilder = filters.toSelectionFilterBuilder();
        } else {
            filterBuilder = SelectionFilter.builder();
        }

        String username = generateService.generate(filterBuilder.build());

        return ResponseEntity.ok().body(username);
    }
}
