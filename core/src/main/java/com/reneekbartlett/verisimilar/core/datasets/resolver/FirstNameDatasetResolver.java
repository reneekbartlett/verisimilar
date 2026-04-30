package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.reneekbartlett.verisimilar.core.datasets.key.FirstNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.FirstNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.selector.engine.FirstNameSelectionEngine.NameKey;

public class FirstNameDatasetResolver extends AbstractDatasetResolver<FirstNameDatasetKey, FirstNameDatasetResult> {

    private static final String DEFAULT_FILE = "datasets/cfg_fullname_first_%s_%s.csv";

    private final Set<GenderIdentity> genderIdentities;
    private final Set<Ethnicity> ethnicities;

    public FirstNameDatasetResolver(ResourceLoaderUtil loader) {
        super(loader);
        this.genderIdentities = GenderIdentity.defaults();
        this.ethnicities = Ethnicity.defaultDatasets();
    }

    @Override
    public FirstNameDatasetResult loadForKey(FirstNameDatasetKey key) {
        
        //key.ethnicity();

        Map<NameKey, Map<String, Double>> datasets = HashMap.newHashMap(0);

        // Load both datasets (male + female) for the given ethnicity
        for(GenderIdentity genderIdentity : genderIdentities) {
            datasets.put(new NameKey(genderIdentity, Ethnicity.UNKNOWN), loadGenderDataset(genderIdentity));
        }

        for(Ethnicity ethnicity : ethnicities) {
            if(ethnicity != Ethnicity.UNKNOWN) {
                genderIdentities.forEach(gender -> {
                    datasets.put(new NameKey(gender, ethnicity), loadEthnicityDataset(gender, ethnicity));
                });
            }
        }

        return new FirstNameDatasetResult(datasets);
    }

    @Override
    public Class<FirstNameDatasetKey> keyType() {
        return FirstNameDatasetKey.class;
    }
    
    @Override
    public Class<FirstNameDatasetResult> resultType() {
        return FirstNameDatasetResult.class;
    }

    private Map<String, Double> loadGenderDataset(GenderIdentity gender) {
        String filePath = String.format(DEFAULT_FILE, gender.name().toLowerCase(), "ALL");
        return load(filePath);
    }

    private Map<String, Double> loadEthnicityDataset(GenderIdentity gender, Ethnicity ethnicity) {
        String filePath = String.format(DEFAULT_FILE, gender.name().toLowerCase(), ethnicity.getPlaceholder());
        return load(filePath);
    }
}
