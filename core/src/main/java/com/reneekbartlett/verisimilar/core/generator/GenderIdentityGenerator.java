package com.reneekbartlett.verisimilar.core.generator;

import com.reneekbartlett.verisimilar.core.datasets.key.GenderIdentityDatasetKey;
import com.reneekbartlett.verisimilar.core.generator.api.AbstractValueGenerator;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.UniformSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class GenderIdentityGenerator extends AbstractValueGenerator<GenderIdentity>{

    public GenderIdentityGenerator() {
        // No SelectionEngine
    }

    @Override
    protected GenderIdentity generateValue(DatasetResolutionContext ctx, SelectionFilter criteria) {
        GenderIdentityDatasetKey key = GenderIdentityDatasetKey.fromContext(ctx);
        return generateGenderIdentity(key, criteria);
    }

    private GenderIdentity generateGenderIdentity(GenderIdentityDatasetKey key, SelectionFilter filter) {
        if(filter != null && filter.equalToMap().containsKey(TemplateField.GENDER_IDENTITY)){
            return GenderIdentity.fromText(filter.equalToMap().get(TemplateField.GENDER_IDENTITY));
        }

        UniformSelectorImpl<GenderIdentity> selector = new UniformSelectorImpl<>(key.genders(), TemplateField.GENDER_IDENTITY);
        return selector.select();
    }

    @Override
    protected Class<GenderIdentity> valueType() {
        return GenderIdentity.class;
    }
}
