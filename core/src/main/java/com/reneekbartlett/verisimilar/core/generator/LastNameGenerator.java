package com.reneekbartlett.verisimilar.core.generator;

import java.util.List;

import com.reneekbartlett.verisimilar.core.datasets.key.LastNameDatasetKey;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.LastNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;

public class LastNameGenerator extends AbstractStringGenerator {
    private final LastNameSelectionEngine selector;
    private final List<TemplateField> filterFields;

    public LastNameGenerator(LastNameSelectionEngine selector) {
        this.selector = selector;
        this.filterFields = List.of(
                TemplateField.FIRST_NAME, TemplateField.MIDDLE_NAME, TemplateField.LAST_NAME, 
                TemplateField.BIRTHDAY, 
                TemplateField.GENDER_IDENTITY,
                TemplateField.REGION, TemplateField.ETHNICITY
        );
    }

    public LastNameGenerator(DatasetSelectionEngineRegistry selectors) {
        this(selectors.last());
    }

    @Override
    protected String generateString(DatasetResolutionContext ctx, SelectionFilter filter) {
        // TODO: LastNameDatasetKey key = new LastNameDatasetKey(ctx.ethnicities().orElse(Set.of(Ethnicity.UNKNOWN)));
        LastNameDatasetKey key = LastNameDatasetKey.fromContext(ctx);
        return generateLastName(key, filter);
    }

    private String generateLastName(LastNameDatasetKey key, SelectionFilter filter) {
        return this.selector.select(key, filter);
    }

    @Override
    public List<TemplateField> filterFields(){
        return this.filterFields;
    }
}
