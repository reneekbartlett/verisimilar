package com.reneekbartlett.verisimilar.core.datasets;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.AreaCodeDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.AreaCodeDatasetResult;
import com.reneekbartlett.verisimilar.core.selector.engine.AreaCodeSelectionEngine.NameKey;

public class AreaCodeFileMapper extends AbstractFileDatasetMapper<AreaCodeDatasetKey, AreaCodeDatasetResult> {

    private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_phone_number_area_code_bystate_%s.csv";
    private final String filePathFormat;

    public AreaCodeFileMapper(String filePathFormat) {
        this.filePathFormat = DEFAULT_FILE_FORMAT;
    }

    public AreaCodeFileMapper() {
        this(DEFAULT_FILE_FORMAT);
    }

    @Override
    public AreaCodeDatasetResult loadFromFile(AreaCodeDatasetKey key, ResourceLoaderUtil loader) {
        Map<NameKey, Map<String, Double>> datasets = loadDatasetsByKey(loader, key);
        return new AreaCodeDatasetResult(datasets);
    }

    private Map<NameKey, Map<String, Double>> loadDatasetsByKey(ResourceLoaderUtil loader, AreaCodeDatasetKey key) {
        String filePath = String.format(filePathFormat, "ALL");
        Map<String, String[]> areaCodesByState = loader.loadArrayMap(filePath);
        Map<NameKey, Map<String, Double>> datasets = HashMap.newHashMap(1);
        areaCodesByState.forEach((state, areaCodeArr) -> {
            Map<String, Double> map = new HashMap<>();
            Arrays.stream(areaCodeArr).forEach(areaCode -> {
                map.put(areaCode, 0.0001);
            });
            datasets.put(new NameKey(state), map);
        });
        return datasets;
    }

    @Override
    protected String resolvePath(AreaCodeDatasetKey key) {
        // TODO Auto-generated method stub
        return null;
    }

}
