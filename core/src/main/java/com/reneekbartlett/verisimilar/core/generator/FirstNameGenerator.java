package com.reneekbartlett.verisimilar.core.generator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.datasets.key.FirstNameDatasetKey;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.FirstNameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;

public class FirstNameGenerator extends AbstractStringGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirstNameGenerator.class);

    private final FirstNameSelectionEngine selector;
    private final List<TemplateField> filterFields;

    public FirstNameGenerator(FirstNameSelectionEngine selector) {
        this.selector = selector;
        this.filterFields = List.of(
                TemplateField.FIRST_NAME, TemplateField.MIDDLE_NAME, TemplateField.LAST_NAME, 
                TemplateField.BIRTHDAY, 
                TemplateField.GENDER_IDENTITY,
                TemplateField.REGION, TemplateField.ETHNICITY
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
        //LOGGER.debug("FirstNameDatasetKey={}", key.toString());
        return selector.select(key, filter);
    }

    @Override
    public List<TemplateField> filterFields(){
        return this.filterFields;
    }

}
