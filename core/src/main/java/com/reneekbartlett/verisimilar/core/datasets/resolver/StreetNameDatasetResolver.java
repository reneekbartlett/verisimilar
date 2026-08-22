package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.StreetNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.StreetNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetNameSelectionEngine.NameKey;

/***
 * For resolving and retrieving StreetName values from dataset files
 * List<String> cfg_postal_address_address1_street_name_ALL.csv
 */
public class StreetNameDatasetResolver extends AbstractDatasetResolver<StreetNameDatasetKey, StreetNameDatasetResult> {

    public static final TemplateField DEFAULT_RETURN_FIELD = TemplateField.STREET_NAME;

    //private static final String DEFAULT_FILE = "datasets/cfg_postal_address_address1_street_name_ALL.csv";
    private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_postal_address_address1_street_name_%s.csv";

    private final String filePathFormat;

    public StreetNameDatasetResolver(ResourceLoaderUtil loader, String filePathFormat) {
        super(loader);
        this.filePathFormat = filePathFormat;
    }

    public StreetNameDatasetResolver(ResourceLoaderUtil loader) {
        this(loader, DEFAULT_FILE_FORMAT);
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
        String defaultFilePath = String.format(filePathFormat, "ALL");
        //LOGGER.debug("Loading default: " + this.defaultDataFile);
        return loader.loadStringArray(defaultFilePath);
    }
}
