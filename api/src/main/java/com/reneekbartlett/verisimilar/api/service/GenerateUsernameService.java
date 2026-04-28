package com.reneekbartlett.verisimilar.api.service;

import com.reneekbartlett.verisimilar.core.generator.UsernameGenerator;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class GenerateUsernameService {

    private final UsernameGenerator generator;

    public GenerateUsernameService(UsernameGenerator generator) {
        this.generator = generator;
    }

    public String generateUsername(
            @RequestParam(required = false)String first, 
            @RequestParam(required = false)String last, 
            @RequestParam(required = false)String region
    ) {

        DatasetResolutionContext context = DatasetResolutionContext.builder()
                .put("firstName", first)
                .put("lastName", last)
                .put("region", region)
                .build();

        SelectionFilter filter = SelectionFilter.builder()
                .firstName(first)
                .build();

        return generator.generate(context, filter);
    }
}
