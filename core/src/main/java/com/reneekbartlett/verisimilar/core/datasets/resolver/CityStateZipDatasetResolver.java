package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.CityStateZipDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.CityStateZipDatasetResult;
import com.reneekbartlett.verisimilar.core.selector.engine.CityStateZipSelectionEngine.NameKey;

public class CityStateZipDatasetResolver extends AbstractDatasetResolver<CityStateZipDatasetKey, CityStateZipDatasetResult> {

    /* Example: MA,Easthampton$MA$01027 */
    private static final String DEFAULT_FILE = "datasets/cfg_postaladdress_citystatezip_usstate_ALL.csv";
    private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_postaladdress_citystatezip_usstate_%s.csv";

    public CityStateZipDatasetResolver(ResourceLoaderUtil loader) {
        super(loader);
    }

    @Override
    public CityStateZipDatasetResult loadForKey(CityStateZipDatasetKey key) {
        Map<String,Double> map = loadValuesAsMap(key);
        return new CityStateZipDatasetResult(Map.of(new NameKey(), map));
    }

    @Override
    public Class<CityStateZipDatasetKey> keyType() {
        return CityStateZipDatasetKey.class;
    }

    @Override
    public Class<CityStateZipDatasetResult> resultType() {
        return CityStateZipDatasetResult.class;
    }

    private Map<String,Double> loadValuesAsMap(CityStateZipDatasetKey key) {
        // TODO:  Use weights from USState enum?  USState[] states = USState.values();
        Map<String,Double> map = new HashMap<>();
        Map<String,String> statesWithCityStateZip = loader.loadStringMap(DEFAULT_FILE, 1);
        for(String v : statesWithCityStateZip.keySet()) {
            map.put(v, 0.0001);
        }
        return map;
    }
}
