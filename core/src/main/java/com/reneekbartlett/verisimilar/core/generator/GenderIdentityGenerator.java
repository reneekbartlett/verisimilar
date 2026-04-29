package com.reneekbartlett.verisimilar.core.generator;

import com.reneekbartlett.verisimilar.core.generator.api.AbstractValueGenerator;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.UniformSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class GenderIdentityGenerator extends AbstractValueGenerator<GenderIdentity>{

    public GenderIdentityGenerator() {
        // No SelectionEngine/GenderIdentityDatasetKey
    }

    @Override
    protected GenderIdentity generateValue(DatasetResolutionContext ctx, SelectionFilter criteria) {
        return generateGenderIdentity(ctx, criteria);
    }

    private GenderIdentity generateGenderIdentity(DatasetResolutionContext ctx, SelectionFilter filter) {
        if(filter != null && filter.equalToMap().containsKey(TemplateField.GENDER_IDENTITY)){
            return GenderIdentity.fromText(filter.equalToMap().get(TemplateField.GENDER_IDENTITY));
        }

        UniformSelectorImpl<GenderIdentity> selector = new UniformSelectorImpl<>(GenderIdentity.defaultMap());
        return selector.select();
    }

    @Override
    protected Class<GenderIdentity> valueType() {
        return GenderIdentity.class;
    }
}
