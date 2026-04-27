package com.reneekbartlett.verisimilar.core.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.generator.api.AbstractValueGenerator;
import com.reneekbartlett.verisimilar.core.model.FullName;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.FirstNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.LastNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.MiddleNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.NameSelectionEngineRegistry;

public class FullNameGenerator extends AbstractValueGenerator<FullName>{
    private static final Logger LOGGER = LoggerFactory.getLogger(FullNameGenerator.class);

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

    public FullNameGenerator(NameSelectionEngineRegistry nameSelectors) {
        this.firstNameGenerator = new FirstNameGenerator(nameSelectors.first());
        this.middleNameGenerator = new MiddleNameGenerator(nameSelectors.middle());
        this.lastNameGenerator = new LastNameGenerator(nameSelectors.last());
    }

    @Override
    protected FullName generateValue(DatasetResolutionContext ctx, SelectionFilter criteria) {
        
        return generateFullName(ctx, criteria);
    }

    @Override
    protected FullName postProcess(FullName record) {
        // Normalize capitalization
        String fn = normalize(record.firstName());
        String mn = normalize(record.middleName());
        String ln = normalize(record.lastName());
        return new FullName(fn, mn, ln);
    }

    private String normalize(String s) {
        if (s == null || s.isBlank()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    @Override
    protected Class<FullName> valueType() {
        return FullName.class;
    }

    /***
     * DatasetResolutionContext:  Dataset routing parameters.Represents global, structural constraints.
     * SelectionFilter:  Carries per-request filtering rules that apply after the dataset is chosen. 
     *     Applied inside selector. Used to refine the candidate list before weighted selection
     * @param ctx
     * @param criteria
     * @return
     */

    private FullName generateFullName(DatasetResolutionContext ctx, SelectionFilter filter) {
        //LOGGER.debug("generateFullName - filter=" + filter.toString());

        String lastName = generateLastName(ctx, filter);

        if(ctx.gender() == null && filter.gender().isEmpty()) {
            // TODO:  update instead of overwrite?
            GenderIdentity genderIdentity = GenderIdentityGenerator.get().generate();
            filter = SelectionFilter.builder().gender(genderIdentity).build();
            LOGGER.debug("generateFullName - Add GenderIdentity to criteria " + genderIdentity.name());
        }

        SelectionFilter firstNameFilter = filter.toBuilder()
                .lastName(lastName)
                .build();
        String firstName = generateFirstName(ctx, firstNameFilter);

        SelectionFilter middleNameFilter = filter.toBuilder()
                .firstName(firstName).lastName(lastName)
                .build();
        String middleName = generateMiddleName(ctx, middleNameFilter);

        return new FullName(firstName, middleName, lastName);
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
}
