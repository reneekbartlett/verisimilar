package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

        Set<Ethnicity> ethnicities = key.ethnicities();
        if(ethnicities.isEmpty()) {
            LOGGER.warn("Empty dataset.");
        }

        Map<NameKey, Map<String, Double>> datasets = HashMap.newHashMap(ethnicities.size());
        for(Ethnicity ethnicity : ethnicities) {
            Map<String, Double> map = loadDataset(ethnicity);
            datasets.put(new NameKey(ethnicity), map);
        }

        return new LastNameDatasetResult(datasets);
    }

    private Map<String, Double> loadDataset(Ethnicity ethnicity) {
        String filePath = String.format(DEFAULT_FILE_FORMAT, ethnicity.getPlaceholder());
        return load(filePath);
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
