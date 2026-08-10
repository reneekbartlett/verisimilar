package com.reneekbartlett.verisimilar.core.generator;

import java.util.List;

import com.reneekbartlett.verisimilar.core.datasets.key.FirstNameDatasetKey;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.FirstNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;

public class FirstNameGenerator extends AbstractStringGenerator {

    private final FirstNameSelectionEngine selector;
    private final List<TemplateField> filterFields;

    public FirstNameGenerator(FirstNameSelectionEngine selector) {
        this.selector = selector;
        this.filterFields = List.of(
                TemplateField.FIRST_NAME, TemplateField.LAST_NAME, 
                TemplateField.BIRTHDAY, 
                TemplateField.GENDER_IDENTITY,
                TemplateField.REGION
        );
    }

    public FirstNameGenerator(DatasetSelectionEngineRegistry selectors) {
        this(selectors.first());
    }

    @Override
    protected String generateString(DatasetResolutionContext ctx, SelectionFilter filter) {
        FirstNameDatasetKey key = FirstNameDatasetKey.fromContext(ctx);
        return generateFirstName(key, filter);
    }

    private String generateFirstName(FirstNameDatasetKey key, SelectionFilter filter) {
        return selector.select(key, filter);
    }

    @Override
    public List<TemplateField> filterFields(){
        return this.filterFields;
    }

}
