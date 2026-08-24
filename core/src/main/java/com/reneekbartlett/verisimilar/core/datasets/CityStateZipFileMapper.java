package com.reneekbartlett.verisimilar.core.datasets;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.CityStateZipDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.CityStateZipDatasetResult;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.engine.CityStateZipSelectionEngine.NameKey;

public class CityStateZipFileMapper extends AbstractFileDatasetMapper<CityStateZipDatasetKey, CityStateZipDatasetResult> {

    public static final TemplateField DEFAULT_RETURN_FIELD = TemplateField.CITY_STATE_ZIP;

    /* Example: MA,Easthampton$MA$01027 */
    private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_postal_address_city_state_zip_us_state_%s.csv";

    private final String filePathFormat;

    public CityStateZipFileMapper(String filePathFormat) {
        this.filePathFormat = DEFAULT_FILE_FORMAT;
    }

    public CityStateZipFileMapper() {
        this(DEFAULT_FILE_FORMAT);
    }

    @Override
    public CityStateZipDatasetResult loadFromFile(CityStateZipDatasetKey key, ResourceLoaderUtil loader) {
        Map<String,Double> map = loadValuesAsMap(loader, key);
        return new CityStateZipDatasetResult(Map.of(new NameKey(), map));
    }

    private Map<String,Double> loadValuesAsMap(ResourceLoaderUtil loader, CityStateZipDatasetKey key) {
        // TODO:  Use weights from USState enum?  USState[] states = USState.values();
        Map<String,Double> map = new HashMap<>();
        String filePath = String.format(filePathFormat, "ALL");
        Map<String,String> statesWithCityStateZip = loader.loadStringMap(filePath, 1);
        for(String v : statesWithCityStateZip.keySet()) {
            map.put(v, 0.0001);
        }
        return map;
    }

    @Override
    protected String resolvePath(CityStateZipDatasetKey key) {
        // TODO Auto-generated method stub
        return null;
    }

}
