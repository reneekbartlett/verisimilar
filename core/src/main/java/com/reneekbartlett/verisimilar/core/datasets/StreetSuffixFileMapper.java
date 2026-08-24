package com.reneekbartlett.verisimilar.core.datasets;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.StreetSuffixDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.StreetSuffixDatasetResult;
//import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetSuffixSelectionEngine.NameKey;

public class StreetSuffixFileMapper extends AbstractFileDatasetMapper<StreetSuffixDatasetKey, StreetSuffixDatasetResult> {

    //private static final TemplateField DEFAULT_FIELD = TemplateField.STREET_SUFFIX;
    private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_postal_address_address1_street_suffix_%s.csv";

    private final String filePathFormat;

    public StreetSuffixFileMapper(String filePathFormat) {
        this.filePathFormat = DEFAULT_FILE_FORMAT;
    }

    public StreetSuffixFileMapper() {
        this(DEFAULT_FILE_FORMAT);
    }

    @Override
    public StreetSuffixDatasetResult loadFromFile(StreetSuffixDatasetKey key, ResourceLoaderUtil loader) {
        // Convert to Weighted Map
        Map<String, String> suffixAbbr = loadDataset(loader, key);
        final Map<String, Double> streetSuffixMap = HashMap.newHashMap(suffixAbbr.size()*2);
        suffixAbbr.forEach((k,v) -> {
            streetSuffixMap.put(k, 1.000);
            streetSuffixMap.put(v, 1.000);
        });

        Map<NameKey, Map<String, Double>> datasets = HashMap.newHashMap(0);
        datasets.put(new NameKey(), streetSuffixMap);

        return new StreetSuffixDatasetResult(datasets);
    }

    //
    private Map<String, String> loadDataset(ResourceLoaderUtil loader, StreetSuffixDatasetKey key) {
        String filePath = String.format(filePathFormat, "ALL");
        //LOGGER.debug("Loading default:" + defaultDataFile);
        return loader.loadStringMap(filePath, 0);
    }

    @Override
    protected String resolvePath(StreetSuffixDatasetKey key) {
        // TODO Auto-generated method stub
        return null;
    }

}
