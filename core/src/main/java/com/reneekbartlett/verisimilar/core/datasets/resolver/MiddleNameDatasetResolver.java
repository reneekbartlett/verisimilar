package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.MiddleNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.MiddleNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.selector.engine.MiddleNameSelectionEngine.NameKey;

public class MiddleNameDatasetResolver extends AbstractDatasetResolver<MiddleNameDatasetKey, MiddleNameDatasetResult> {

    private static final String DEFAULT_FILE = "datasets/cfg_fullname_middle_%s_%s.csv";

    private final EnumSet<GenderIdentity> genderIdentities;
    private final EnumSet<Ethnicity> ethnicities;

    public MiddleNameDatasetResolver(ResourceLoaderUtil loader) {
        super(loader);
        this.genderIdentities = GenderIdentity.defaults();
        this.ethnicities = Ethnicity.defaultDatasets();
    }

    @Override
    public MiddleNameDatasetResult loadForKey(MiddleNameDatasetKey key) {
        Map<NameKey, Map<String, Double>> datasets = HashMap.newHashMap(0);

        for(GenderIdentity genderIdentity : genderIdentities) {
            datasets.put(new NameKey(genderIdentity, Ethnicity.UNKNOWN), loadGenderDataset(genderIdentity));
        }

        // TODO: Add Ethnicity Datasets
        // Use culture data on whether or not to populate based on ethnicity
        for(Ethnicity ethnicity : ethnicities) {
            if(ethnicity != Ethnicity.UNKNOWN) {
                // TODO
                //for(GenderIdentity genderIdentity : genderIdentities) {
                //    //datasets.put(new NameKey(genderIdentity, Ethnicity.UNKNOWN), loadEthnicityDataset(genderIdentity, ethnicity));
                //}
            }
        }

        return new MiddleNameDatasetResult(datasets);
    }

    private Map<String, Double> loadGenderDataset(GenderIdentity gender) {
        String filePath = String.format(DEFAULT_FILE, gender.name().toLowerCase(), "ALL");
        return load(filePath);
    }

    @SuppressWarnings("unused")
    private Map<String, Double> loadEthnicityDataset(GenderIdentity gender, Ethnicity ethnicity) {
        String filePath = String.format(DEFAULT_FILE, gender.name().toLowerCase(), ethnicity.getPlaceholder());
        if(!exists(filePath)) {
            // TODO:  Warn/add placeholders
            return HashMap.newHashMap(0);
        }
        return load(filePath);
    }

    @Override
    public Class<MiddleNameDatasetKey> keyType() {
        return MiddleNameDatasetKey.class;
    }

    @Override
    public Class<MiddleNameDatasetResult> resultType() {
        return MiddleNameDatasetResult.class;
    }
}
