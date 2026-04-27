package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.MiddleNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.MiddleNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;

public class MiddleNameDatasetResolver extends AbstractDatasetResolver<MiddleNameDatasetKey, MiddleNameDatasetResult> {

    private static final String DEFAULT_FILE = "datasets/cfg_fullname_middle_%s_ALL.csv";

    public MiddleNameDatasetResolver(ResourceLoaderUtil loader) {
        super(loader);
    }

    @Override
    public MiddleNameDatasetResult loadForKey(MiddleNameDatasetKey key) {
        Map<String, Double> female = loadGenderDataset(GenderIdentity.FEMALE, key);
        Map<String, Double> male = loadGenderDataset(GenderIdentity.MALE, key);
        return new MiddleNameDatasetResult(female, male);
    }

    private Map<String, Double> loadGenderDataset(GenderIdentity gender, MiddleNameDatasetKey key) {
        String genderStr = gender.name().toLowerCase();
        String fallback = String.format(DEFAULT_FILE, genderStr);
        //LOGGER.debug("Using Default/Fallback config Path: " + fallback);
        return load(fallback);
    }

    @Override
    public Class<MiddleNameDatasetKey> keyType() {
        return MiddleNameDatasetKey.class;
    }
    
    @Override
    public Class<MiddleNameDatasetResult> resultType() {
        return MiddleNameDatasetResult.class;
    }
}
