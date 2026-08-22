package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.StreetSuffixDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.StreetSuffixDatasetResult;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetSuffixSelectionEngine.NameKey;

/***
 * Use for resolving and retrieving STREET_SUFFIX values from dataset files
 */
public class StreetSuffixDatasetResolver extends AbstractDatasetResolver<StreetSuffixDatasetKey, StreetSuffixDatasetResult> {

    private static final TemplateField DEFAULT_FIELD = TemplateField.STREET_SUFFIX;
    private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_postal_address_address1_street_suffix_%s.csv";

    private final String filePathFormat;
    @SuppressWarnings("unused")
    private final TemplateField defaultReturnField;

    public StreetSuffixDatasetResolver(ResourceLoaderUtil loader, String filePathFormat) {
        super(loader);
        this.filePathFormat = filePathFormat;
        this.defaultReturnField = DEFAULT_FIELD;
    }

    public StreetSuffixDatasetResolver(ResourceLoaderUtil loader) {
        this(loader, DEFAULT_FILE_FORMAT);
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
        String filePath = String.format(filePathFormat, "ALL");
        //LOGGER.debug("Loading default:" + defaultDataFile);
        return loader.loadStringMap(filePath, 0);
    }
}
