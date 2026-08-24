package com.reneekbartlett.verisimilar.core.generator;

import java.util.EnumSet;

import com.reneekbartlett.verisimilar.core.datasets.key.GenderIdentityDatasetKey;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.WeightedEnumData;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.UniformSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class GenderIdentityGenerator extends AbstractEnumGenerator<GenderIdentity>{

    public GenderIdentityGenerator() {
        // No SelectionEngine
        super(WeightedEnumData.defaultSet(GenderIdentity.class));
    }
    
    public GenderIdentityGenerator(EnumSet<GenderIdentity> enumSet) {
        // No SelectionEngine
        super(enumSet);
    }

    @Override
    protected GenderIdentity generateEnum(DatasetResolutionContext ctx, SelectionFilter criteria) {
        GenderIdentityDatasetKey key = GenderIdentityDatasetKey.fromContext(ctx);
        return generateGenderIdentity(key, criteria);
    }

    private GenderIdentity generateGenderIdentity(GenderIdentityDatasetKey key, SelectionFilter filter) {
        return filter.gender().orElseGet(() -> {
            UniformSelectorImpl<GenderIdentity> selector = new UniformSelectorImpl<>(key.genders(), TemplateField.GENDER_IDENTITY);
            return selector.select();
        });
    }

    @Override
    protected Class<GenderIdentity> valueType() {
        return GenderIdentity.class;
    }
}
