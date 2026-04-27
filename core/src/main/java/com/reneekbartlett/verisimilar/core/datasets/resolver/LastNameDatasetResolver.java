package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.LastNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.LastNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.selector.engine.LastNameSelectionEngine.NameKey;

public class LastNameDatasetResolver extends AbstractDatasetResolver<LastNameDatasetKey, LastNameDatasetResult> {

    // "datasets/cfg_fullname_last_%s.csv"
    private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_fullname_last_%s.csv";

    public LastNameDatasetResolver(ResourceLoaderUtil loader) {
        super(loader);
    }

    /***
     * Function passed to DatasetCache. 
     */
    @Override
    public LastNameDatasetResult loadForKey(LastNameDatasetKey key) {
        //LOGGER.debug("loadForKey - key:{}", key);
        Map<NameKey, Map<String, Double>> datasets = loadDatasetsByKey(key);
        return new LastNameDatasetResult(datasets);
    }

    private Map<NameKey, Map<String, Double>> loadDatasetsByKey(LastNameDatasetKey key) {
        Map<NameKey, Map<String, Double>> datasets = HashMap.newHashMap(1);
        if(key.ethnicity() != null) {
            // datasets/cfg_fullname_last_%s.csv
            String fileKey = key.ethnicity().equals(Ethnicity.UNKNOWN) ? "ALL" : key.ethnicity().name();
            String path = String.format(DEFAULT_FILE_FORMAT, fileKey);
            if (exists(path)) {
                NameKey eKey = new NameKey(key.ethnicity());
                Map<String, Double> eMap = super.load(path);
                datasets.put(eKey, eMap);
            } else {
                LOGGER.warn("LastName Config file doesn't exist: " + path);
            }
        }
        return datasets;
    }

    @Override
    public Class<LastNameDatasetKey> keyType() {
        return LastNameDatasetKey.class;
    }

    @Override
    public Class<LastNameDatasetResult> resultType() {
        return LastNameDatasetResult.class;
    }
}
