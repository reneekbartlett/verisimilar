package com.reneekbartlett.verisimilar.core.datasets.resolver;

import com.reneekbartlett.verisimilar.core.datasets.key.StreetNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.StreetNameDatasetResult;

/***
 * List<String> cfg_postaladdress_address1_streetname_ALL.csv
 */
public class StreetNameDatasetResolver extends AbstractDatasetResolver<StreetNameDatasetKey, StreetNameDatasetResult> {

    private static final String DEFAULT_FILE = "datasets/cfg_postaladdress_address1_streetname_ALL.csv";

    public StreetNameDatasetResolver(ResourceLoaderUtil loader) {
        super(loader);
    }

    @Override
    public StreetNameDatasetResult loadForKey(StreetNameDatasetKey key) {
        String[] all = loadValues(key);
        return new StreetNameDatasetResult(all);
    }

    @Override
    public Class<StreetNameDatasetKey> keyType() {
        return StreetNameDatasetKey.class;
    }
    
    @Override
    public Class<StreetNameDatasetResult> resultType() {
        return StreetNameDatasetResult.class;
    }

    //private Map<String, Double> loadDataset(StreetNameDatasetKey key) {
    //    LOGGER.debug("Loading default:" + DEFAULT_FILE);
    //    return loader.loadWeightedMap(DEFAULT_FILE);
    //}

    private String[] loadValues(StreetNameDatasetKey key) {
        //LOGGER.debug("Loading default: " + DEFAULT_FILE);
        return loader.loadStringArray(DEFAULT_FILE);
    }
}
