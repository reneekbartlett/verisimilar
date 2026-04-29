package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.StreetSuffixDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.StreetSuffixDatasetResult;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetSuffixSelectionEngine.NameKey;

public class StreetSuffixDatasetResolver extends AbstractDatasetResolver<StreetSuffixDatasetKey, StreetSuffixDatasetResult> {

    private static final String DEFAULT_FILE = "datasets/cfg_postaladdress_address1_streetsuffix_ALL.csv";

    public StreetSuffixDatasetResolver(ResourceLoaderUtil loader) {
        super(loader);
    }

    @Override
    public StreetSuffixDatasetResult loadForKey(StreetSuffixDatasetKey key) {
        // Convert to Weighted Map
        Map<String, String> suffixAbbr = loadDataset(key);
        final Map<String, Double> streetSuffixMap = HashMap.newHashMap(suffixAbbr.size()*2);
        suffixAbbr.forEach((k,v) -> {
            streetSuffixMap.put(k, 1.000);
            streetSuffixMap.put(v, 1.000);
        });

        Map<NameKey, Map<String, Double>> datasets = HashMap.newHashMap(0);
        datasets.put(new NameKey(), streetSuffixMap);

        return new StreetSuffixDatasetResult(datasets);
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
