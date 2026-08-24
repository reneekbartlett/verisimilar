package com.reneekbartlett.verisimilar.core.datasets;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.reneekbartlett.verisimilar.core.datasets.key.MiddleNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.MiddleNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.selector.engine.MiddleNameSelectionEngine.NameKey;

public class MiddleNameFileMapper extends AbstractFileDatasetMapper<MiddleNameDatasetKey, MiddleNameDatasetResult> {

    private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_full_name_first_name_%s_%s.csv";

    private final String filePathFormat;

    public MiddleNameFileMapper(String filePathFormat) {
        this.filePathFormat = DEFAULT_FILE_FORMAT;
    }

    public MiddleNameFileMapper() {
        this(DEFAULT_FILE_FORMAT);
    }

    @Override
    public MiddleNameDatasetResult loadFromFile(MiddleNameDatasetKey key, ResourceLoaderUtil loader) {

        Set<GenderIdentity> genderIdentities = GenderIdentity.defaults();
        Set<Ethnicity> ethnicities = Ethnicity.defaultDatasets();
        //Set<Decade> decades = Decade.defaultDatasets();

        Map<NameKey, Map<String, Double>> datasets = HashMap.newHashMap(0);

        for(GenderIdentity genderIdentity : genderIdentities) {
            datasets.put(new NameKey(genderIdentity, Ethnicity.UNKNOWN), loadGenderDataset(loader, genderIdentity));
        }

        // TODO: Add Ethnicity Datasets
        // Use culture data on whether or not to populate based on ethnicity
        for(Ethnicity ethnicity : ethnicities) {
            if(ethnicity != Ethnicity.UNKNOWN) {
                // TODO
                //for(GenderIdentity genderIdentity : genderIdentities) {
                //    //datasets.put(new NameKey(genderIdentity, Ethnicity.UNKNOWN), loadEthnicityDataset(loader, genderIdentity, ethnicity));
                //}
            }
        }

        return new MiddleNameDatasetResult(datasets);
    }

    private Map<String, Double> loadGenderDataset(ResourceLoaderUtil loader, GenderIdentity gender) {
        String filePath = String.format(filePathFormat, gender.name().toLowerCase(), "ALL");
        return load(loader, filePath);
    }

    @SuppressWarnings("unused")
    private Map<String, Double> loadEthnicityDataset(ResourceLoaderUtil loader, GenderIdentity gender, Ethnicity ethnicity) {
        String filePath = String.format(filePathFormat, gender.name().toLowerCase(), ethnicity.getPlaceholder());
        if(!loader.exists(filePath)) {
            // TODO:  Warn/add placeholders
            return HashMap.newHashMap(0);
        }
        return super.load(loader, filePath);
    }

    @Override
    protected String resolvePath(MiddleNameDatasetKey key) {
        // TODO Auto-generated method stub
        return null;
    }
}
