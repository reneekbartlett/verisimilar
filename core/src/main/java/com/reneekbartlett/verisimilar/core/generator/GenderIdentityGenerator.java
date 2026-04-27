package com.reneekbartlett.verisimilar.core.generator;

import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.generator.api.AbstractValueGenerator;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class GenderIdentityGenerator extends AbstractValueGenerator<GenderIdentity>{
    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(GenderIdentityGenerator.class);

    public GenderIdentityGenerator() {
        //
    }

    @Override
    protected GenderIdentity generateValue(DatasetResolutionContext ctx, SelectionFilter criteria) {
        return generateGenderIdentity(ctx, criteria);
    }

    private GenderIdentity generateGenderIdentity(DatasetResolutionContext ctx, SelectionFilter criteria) {
        // TODO: Random
        GenderIdentity[] options = new GenderIdentity[] { GenderIdentity.FEMALE, GenderIdentity.MALE };
        //GenderIdentity.values();
        return options[ThreadLocalRandom.current().nextInt(options.length)];
    }

    @Override
    protected Class<GenderIdentity> valueType() {
        return GenderIdentity.class;
    }

    public static GenderIdentityGenerator get() {
        return new GenderIdentityGenerator();
    }

}
