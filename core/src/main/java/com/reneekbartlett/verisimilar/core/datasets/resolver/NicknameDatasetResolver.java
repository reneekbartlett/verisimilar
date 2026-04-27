package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.datasets.key.NicknameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.NicknameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;

public class NicknameDatasetResolver extends AbstractDatasetResolver<NicknameDatasetKey, NicknameDatasetResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NicknameDatasetResolver.class);
    private static final String DEFAULT_FILE = "datasets/cfg_nicknames_%s_ALL.csv";

    public NicknameDatasetResolver(ResourceLoaderUtil loader) {
        super(loader);
    }

    @Override
    public NicknameDatasetResult loadForKey(NicknameDatasetKey key) {
        Map<String, Double> female = loadGenderDataset(GenderIdentity.FEMALE, key);
        Map<String, Double> male = loadGenderDataset(GenderIdentity.MALE, key);

        return new NicknameDatasetResult(female, male);
    }

    private Map<String, Double> loadGenderDataset(
            GenderIdentity gender,
            NicknameDatasetKey key
    ) {
        String genderStr = gender.name().toLowerCase();

        // 3. fallback: gender + ALL
        String fallback = String.format(DEFAULT_FILE, genderStr);
        LOGGER.debug("Default Path: " + fallback);
        return load(fallback);
    }

    @Override
    public Class<NicknameDatasetKey> keyType() {
        return NicknameDatasetKey.class;
    }

    @Override
    public Class<NicknameDatasetResult> resultType() {
        return NicknameDatasetResult.class;
    }
}
