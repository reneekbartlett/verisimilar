package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.AreaCodeDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.AreaCodeDatasetResult;
import com.reneekbartlett.verisimilar.core.selector.engine.AreaCodeSelectionEngine.NameKey;

public class AreaCodeDatasetResolver extends AbstractDatasetResolver<AreaCodeDatasetKey, AreaCodeDatasetResult> {

    private static final String DEFAULT_FILE = "datasets/cfg_phonenumber_areacode_bystate_ALL.csv";

    /***
     * Map<String, String[]> AREACODES_BY_STATE = ResourceMapLoader.loadArrayMap("/cfg_phonenumber_areacode_bystate_ALL.csv");
     * AZ,480|520|602|623|928
     */
    public AreaCodeDatasetResolver(ResourceLoaderUtil loader) {
        super(loader);
    }

    @Override
    public AreaCodeDatasetResult loadForKey(AreaCodeDatasetKey key) {
        Map<String, String[]> areaCodesByState = loadDataset(key);

        Map<NameKey, Map<String, Double>> datasets = loadDatasetsByKey(key);

        return new AreaCodeDatasetResult(areaCodesByState, datasets);
    }

    private Map<NameKey, Map<String, Double>> loadDatasetsByKey(AreaCodeDatasetKey key) {
        Map<NameKey, Map<String, Double>> datasets = HashMap.newHashMap(1);

        Map<String, String[]> m1 = loader.loadArrayMap(DEFAULT_FILE);

        m1.forEach((state, areaCodeArr) -> {
            Map<String, Double> map = new HashMap<>();
            Arrays.stream(areaCodeArr).forEach(areaCode -> {
                map.put(areaCode, 0.0001);
            });
            datasets.put(new NameKey(state), map);
        });

        return datasets;
    }

    private Map<String, String[]> loadDataset(AreaCodeDatasetKey key) {
        //LOGGER.debug("Default path: " + DEFAULT_FILE);
        return loader.loadArrayMap(DEFAULT_FILE);
    }

    @Override
    public Class<AreaCodeDatasetKey> keyType() {
        return AreaCodeDatasetKey.class;
    }

    @Override
    public Class<AreaCodeDatasetResult> resultType() {
        return AreaCodeDatasetResult.class;
    }

    
}
