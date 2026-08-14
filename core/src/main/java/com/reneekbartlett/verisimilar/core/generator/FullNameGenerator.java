package com.reneekbartlett.verisimilar.core.generator;

import java.util.ArrayList;
import java.util.List;

import com.reneekbartlett.verisimilar.core.model.FullName;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.FirstNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.LastNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.MiddleNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;

public class FullNameGenerator extends AbstractValueGenerator<FullName>{
    private final FirstNameGenerator firstNameGenerator;
    private final MiddleNameGenerator middleNameGenerator;
    private final LastNameGenerator lastNameGenerator;

    public FullNameGenerator(
            FirstNameSelectionEngine firstNameSelector,
            MiddleNameSelectionEngine middleNameSelector,
            LastNameSelectionEngine lastNameSelector) {
        this.firstNameGenerator = new FirstNameGenerator(firstNameSelector);
        this.middleNameGenerator = new MiddleNameGenerator(middleNameSelector);
        this.lastNameGenerator = new LastNameGenerator(lastNameSelector);
    }

    public FullNameGenerator(DatasetSelectionEngineRegistry nameSelectors) {
        this(nameSelectors.first(), nameSelectors.middle(), nameSelectors.last());
    }

    @Override
    protected FullName generateValue(DatasetResolutionContext ctx, SelectionFilter filter) {
        //
        // Generate LastName first.
        String lastName = filter.lastName().orElseGet(() -> generateLastName(ctx, filter));

        //  Get or Generate GenderIdentity.
        GenderIdentity genderIdentity = filter.gender()
                .orElseGet(() -> {
                    return new GenderIdentityGenerator().generate(ctx, filter);
                });
        //LOGGER.debug("FullNameGenerator.generateValue - Add GenderIdentity to filter: {}", genderIdentity.name());

        //
        //  Get or Generate FirstName.  Include LastName in generator params.
        String firstName = filter.firstName()
                .orElseGet(() -> {
                    SelectionFilter firstNameFilter = SelectionFilter.toBuilder(filter)
                            .lastName(lastName)
                            .gender(genderIdentity)
                            .build();
                    return generateFirstName(ctx, firstNameFilter);
                });

        //
        //  Generate MiddleName.  Include FirstName+LastName+GenderIdentity in generator params.
        String middleName = filter.middleName()
                .orElseGet(() -> {
                    SelectionFilter middleNameFilter = SelectionFilter.toBuilder(filter)
                            .firstName(firstName).lastName(lastName).gender(genderIdentity)
                            .build();
                    return generateMiddleName(ctx, middleNameFilter);
                });

        return new FullName(firstName, middleName, lastName, genderIdentity);
    }

    private String generateFirstName(DatasetResolutionContext ctx, SelectionFilter filter) {
        return firstNameGenerator.generate(ctx, filter);
    }

    private String generateMiddleName(DatasetResolutionContext ctx, SelectionFilter filter) {
        return middleNameGenerator.generate(ctx, filter);
    }

    private String generateLastName(DatasetResolutionContext ctx, SelectionFilter filter) {
        return lastNameGenerator.generate(ctx, filter);
    }

    @Override
    protected Class<FullName> valueType() {
        return FullName.class;
    }

    @Override
    public List<TemplateField> filterFields(){
        List<TemplateField> filterFields = new ArrayList<>();
        filterFields.addAll(firstNameGenerator.filterFields());
        filterFields.addAll(middleNameGenerator.filterFields());
        filterFields.addAll(lastNameGenerator.filterFields());
        return filterFields;
    }
}
