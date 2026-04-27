package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.datasets.key.FirstNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.FirstNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;

public class FirstNameDatasetResolver extends AbstractDatasetResolver<FirstNameDatasetKey, FirstNameDatasetResult> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(FirstNameDatasetResolver.class);
    private static final String DEFAULT_FILE = "datasets/cfg_fullname_first_%s_ALL.csv";

    public FirstNameDatasetResolver(ResourceLoaderUtil loader) {
        super(loader);
    }

    @Override
    public FirstNameDatasetResult loadForKey(FirstNameDatasetKey key) {
        // Load both datasets (male + female) for the given ethnicity
        // You load everything up front — selection happens later.
        Map<String, Double> female = loadGenderDataset(GenderIdentity.FEMALE, key);
        Map<String, Double> male = loadGenderDataset(GenderIdentity.MALE, key);
        
        // TODO
        
        return new FirstNameDatasetResult(female, male);
    }

    @Override
    public Class<FirstNameDatasetKey> keyType() {
        return FirstNameDatasetKey.class;
    }
    
    @Override
    public Class<FirstNameDatasetResult> resultType() {
        return FirstNameDatasetResult.class;
    }

    private Map<String, Double> loadGenderDataset(GenderIdentity gender, FirstNameDatasetKey key) {
        String filePath = String.format(DEFAULT_FILE, gender.name().toLowerCase());
        return load(filePath);
    }
}
