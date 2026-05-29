package com.reneekbartlett.verisimilar.api.controller;

import com.reneekbartlett.verisimilar.api.service.GenerateEmailAddressService;
import com.reneekbartlett.verisimilar.api.util.JsonApiParser;
import com.reneekbartlett.verisimilar.api.util.JsonApiParser.FilterConditions;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.UsernameType;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generate/email")
public class GenerateEmailAddressController {

    private final GenerateEmailAddressService generateService;
    private final List<TemplateField> filterFields;
    private final JsonApiParser jsonRequestParser;

    public GenerateEmailAddressController(GenerateEmailAddressService generateService) {
        this.generateService = generateService;
        this.filterFields = List.of(TemplateField.FIRST_NAME, TemplateField.MIDDLE_NAME, TemplateField.LAST_NAME, 
                TemplateField.STATE, TemplateField.GENDER_IDENTITY, 
                TemplateField.DOMAIN, TemplateField.DOMAIN_TYPE,
                TemplateField.USERNAME, TemplateField.USERNAME_TYPE);
        this.jsonRequestParser = new JsonApiParser(this.filterFields);
    }

    @GetMapping
    public ResponseEntity<Object> generate(
            @RequestParam(name="gender", required=false) String gender,
            @RequestParam(name="fname", required=false) String fname,
            @RequestParam(name="mname", required=false) String mname,
            @RequestParam(name="lname", required=false) String lname,
            @RequestParam(name="birthday", required=false) String birthday,
            @RequestParam(name="domain", required=false) String domain,
            @RequestParam(name="domain_type", required=false) String domain_type,
            @RequestParam(name="username", required=false) String username,
            @RequestParam(name="username_type", required=false) String username_type,
            HttpServletRequest request
    ) {

        SelectionFilter.Builder filterBuilder;
        FilterConditions filters = jsonRequestParser.parse(request.getParameterMap());
        if (filters.size() > 0) {
            filterBuilder = filters.toSelectionFilterBuilder();
        } else {
            filterBuilder = SelectionFilter.builder();
        }

        // TODO:  Validate values.  Empty/null handling.
        if(GenderIdentity.fromText(gender) != null) filterBuilder.gender(GenderIdentity.fromText(gender));
        if(fname != null) filterBuilder.firstName(fname);
        if(mname != null) filterBuilder.middleName(mname);
        if(lname != null) filterBuilder.lastName(lname);

        if(domain != null) filterBuilder.domain(domain);
        if(domain_type != null) filterBuilder.domainType(DomainType.valueOf(domain_type));

        if(username != null) filterBuilder.username(username);
        if(username_type != null) filterBuilder.usernameType(UsernameType.valueOf(username_type));

        var email = generateService.generate(filterBuilder.build());

        return ResponseEntity.ok().body(email);
    }
}
