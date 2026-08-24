package com.reneekbartlett.verisimilar.core.datasets;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.StreetNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.StreetNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetNameSelectionEngine.NameKey;

public class StreetNameFileMapper extends AbstractFileDatasetMapper<StreetNameDatasetKey, StreetNameDatasetResult> {

    public static final TemplateField DEFAULT_RETURN_FIELD = TemplateField.STREET_NAME;
    private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_postal_address_address1_street_name_%s.csv";

    private final String filePathFormat;

    public StreetNameFileMapper(String filePathFormat) {
        this.filePathFormat = DEFAULT_FILE_FORMAT;
    }

    public StreetNameFileMapper() {
        this(DEFAULT_FILE_FORMAT);
    }

    @Override
    public StreetNameDatasetResult loadFromFile(StreetNameDatasetKey key, ResourceLoaderUtil loader) {
        String[] all = loadValues(loader, key);

        // Convert to Weighted Map
        final Map<String, Double> streetNameMap = HashMap.newHashMap(all.length);
        for(String val : all) {
            streetNameMap.put(val, 0.0001);
        }

        Map<NameKey, Map<String, Double>> datasets = HashMap.newHashMap(1);
        datasets.put(new NameKey(), streetNameMap);

        return new StreetNameDatasetResult(datasets);
    }

    // TODO
    private String[] loadValues(ResourceLoaderUtil loader, StreetNameDatasetKey key) {
        String defaultFilePath = String.format(filePathFormat, "ALL");
        //LOGGER.debug("Loading default: " + this.defaultDataFile);
        return loader.loadStringArray(defaultFilePath);
    }

    @Override
    protected String resolvePath(StreetNameDatasetKey key) {
        // TODO Auto-generated method stub
        return null;
    }

}
