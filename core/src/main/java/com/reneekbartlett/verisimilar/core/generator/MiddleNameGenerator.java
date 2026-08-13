package com.reneekbartlett.verisimilar.core.generator;

import java.util.List;

import com.reneekbartlett.verisimilar.core.datasets.key.MiddleNameDatasetKey;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.MiddleNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;

public class MiddleNameGenerator extends AbstractStringGenerator {
    private final MiddleNameSelectionEngine selector;
    private final List<TemplateField> filterFields;

    public MiddleNameGenerator(MiddleNameSelectionEngine selector) {
        this.selector = selector;
        this.filterFields = List.of(
                TemplateField.FIRST_NAME, TemplateField.MIDDLE_NAME, TemplateField.LAST_NAME, 
                TemplateField.BIRTHDAY, 
                TemplateField.GENDER_IDENTITY,
                TemplateField.REGION, TemplateField.ETHNICITY
        );
    }

    public MiddleNameGenerator(DatasetSelectionEngineRegistry selectors) {
        this(selectors.middle());
    }

    @Override
    protected String generateString(DatasetResolutionContext ctx, SelectionFilter filter) {
        MiddleNameDatasetKey key = MiddleNameDatasetKey.fromContext(ctx);
        return generateMiddleName(key, filter);
    }

    private String generateMiddleName(MiddleNameDatasetKey key, SelectionFilter filter) {
        return selector.select(key, filter);
    }

    @Override
    public List<TemplateField> filterFields(){
        return this.filterFields;
    }
}
