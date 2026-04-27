package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.StreetSuffixDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.StreetSuffixDatasetResult;

public class StreetSuffixDatasetResolver extends AbstractDatasetResolver<StreetSuffixDatasetKey, StreetSuffixDatasetResult> {

    private static final String DEFAULT_FILE = "datasets/cfg_postaladdress_address1_streetsuffix_ALL.csv";

    public StreetSuffixDatasetResolver(ResourceLoaderUtil loader) {
        super(loader);
    }

    @Override
    public StreetSuffixDatasetResult loadForKey(StreetSuffixDatasetKey key) {
        Map<String, String> suffixAbbr = loadDataset(key);
        return new StreetSuffixDatasetResult(suffixAbbr);
    }

    @Override
    public Class<StreetSuffixDatasetKey> keyType() {
        return StreetSuffixDatasetKey.class;
    }

    @Override
    public Class<StreetSuffixDatasetResult> resultType() {
        return StreetSuffixDatasetResult.class;
    }

    private Map<String, String> loadDataset(StreetSuffixDatasetKey key) {
        //LOGGER.debug("Loading default:" + DEFAULT_FILE);
        return loader.loadStringMap(DEFAULT_FILE, 0);
    }
}
