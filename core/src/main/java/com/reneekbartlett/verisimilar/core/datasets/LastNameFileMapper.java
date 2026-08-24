package com.reneekbartlett.verisimilar.core.datasets;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.LastNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.LastNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.selector.engine.LastNameSelectionEngine.NameKey;

public class LastNameFileMapper extends AbstractFileDatasetMapper<LastNameDatasetKey, LastNameDatasetResult> {
    private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_full_name_last_name_%s.csv";

    private final String filePathFormat;

    public LastNameFileMapper(String filePathFormat) {
        this.filePathFormat = DEFAULT_FILE_FORMAT;
    }

    public LastNameFileMapper() {
        this(DEFAULT_FILE_FORMAT);
    }

    @Override
    public LastNameDatasetResult loadFromFile(LastNameDatasetKey key, ResourceLoaderUtil loader) {
        //LOGGER.debug("loadForKey - key:{}", key);

        EnumSet<Ethnicity> ethnicities;
        if(!key.ethnicities().isEmpty())
            ethnicities = key.ethnicities();
        else {
            //LOGGER.warn("Empty dataset.");
            //ethnicities = this.ethnicities;
            ethnicities = Ethnicity.defaultDatasets();
        }

        Map<NameKey, Map<String, Double>> datasets = HashMap.newHashMap(ethnicities.size());
        for(Ethnicity ethnicity : ethnicities) {
            Map<String, Double> map = loadDataset(loader, ethnicity);
            datasets.put(new NameKey(ethnicity), map);
        }

        return new LastNameDatasetResult(datasets);
    }

    private Map<String, Double> loadDataset(ResourceLoaderUtil loader, Ethnicity ethnicity) {
        String filePath = String.format(filePathFormat, ethnicity.getPlaceholder());
        return super.load(loader, filePath);
    }

    @Override
    protected String resolvePath(LastNameDatasetKey key) {
        // TODO Auto-generated method stub
        return null;
    }
}
