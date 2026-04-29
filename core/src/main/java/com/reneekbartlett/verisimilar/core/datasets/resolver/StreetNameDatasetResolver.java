package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.StreetNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.StreetNameDatasetResult;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetNameSelectionEngine.NameKey;

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

        // Convert to Weighted Map
        final Map<String, Double> streetNameMap = HashMap.newHashMap(all.length);
        for(String val : all) {
            streetNameMap.put(val, 0.0001);
        }

        Map<NameKey, Map<String, Double>> datasets = HashMap.newHashMap(1);
        datasets.put(new NameKey(), streetNameMap);

        return new StreetNameDatasetResult(datasets);
    }

    @Override
    public Class<StreetNameDatasetKey> keyType() {
        return StreetNameDatasetKey.class;
    }

    @Override
    public Class<StreetNameDatasetResult> resultType() {
        return StreetNameDatasetResult.class;
    }

    private String[] loadValues(StreetNameDatasetKey key) {
        //LOGGER.debug("Loading default: " + DEFAULT_FILE);
        return loader.loadStringArray(DEFAULT_FILE);
    }
}
