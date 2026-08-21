package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.FirstNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.FirstNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.Decade;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.selector.engine.FirstNameSelectionEngine.NameKey;

/***
 * 
 */
public class FirstNameDatasetResolver extends AbstractDatasetResolver<FirstNameDatasetKey, FirstNameDatasetResult> {

    private static final String DEFAULT_FILE = "datasets/cfg_fullname_first_%s_%s.csv";
    private static final String DEFAULT_FILE_V2 = "datasets/cfg_fullname_first_%s_%s_%s.csv";

    private final EnumSet<GenderIdentity> genderIdentities;
    private final EnumSet<Ethnicity> ethnicities;
    private final EnumSet<Decade> decades;

    public FirstNameDatasetResolver(ResourceLoaderUtil loader) {
        super(loader);
        this.genderIdentities = GenderIdentity.defaults();
        this.ethnicities = Ethnicity.defaultDatasets();
        this.decades = Decade.defaultDatasets();
    }

    @Override
    public FirstNameDatasetResult loadForKey(FirstNameDatasetKey key) {
        Map<NameKey, Map<String, Double>> datasets = HashMap.newHashMap(0);

        // Load datasets that have ethnicity data (male + female) for the given ethnicity
        for(GenderIdentity genderIdentity : genderIdentities) {
            datasets.put(new NameKey(genderIdentity, Ethnicity.UNKNOWN, Decade.ALL), loadGenderDataset(genderIdentity));
        }

        // Load Unisex names, without ethnicity for now.
        if(GenderIdentity.defaultDatasets().contains(GenderIdentity.GENDER_UNSPECIFIED)) {
            datasets.put(new NameKey(GenderIdentity.GENDER_UNSPECIFIED, Ethnicity.UNKNOWN, Decade.ALL), loadGenderDataset(GenderIdentity.GENDER_UNSPECIFIED));
        }

        for(Decade decade : decades) {
            if(decade != Decade.ALL) {
                genderIdentities.forEach(gender -> {
                    //datasets.put(new NameKey(gender, decade), loadDecadeDataset(gender, decade));
                    // TODO:  Add Decade to NameKey
                });
            }
        }

        for(Ethnicity ethnicity : ethnicities) {
            if(ethnicity != Ethnicity.UNKNOWN) {
                genderIdentities.forEach(gender -> {
                    datasets.put(new NameKey(gender, ethnicity, Decade.ALL), loadEthnicityDataset(gender, ethnicity));
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
        String filePath = String.format(DEFAULT_FILE, gender.getPlaceholder().toLowerCase(), "ALL");
        return load(filePath);
    }

    @SuppressWarnings("unused")
    private Map<String, Double> loadDecadeDataset(GenderIdentity gender, Decade decade, Ethnicity ethnicity) {
        String filePath = String.format(DEFAULT_FILE_V2, gender.getPlaceholder().toLowerCase(), decade.getPlaceholder());
        return load(filePath);
    }

    private Map<String, Double> loadEthnicityDataset(GenderIdentity gender, Ethnicity ethnicity) {
        String filePath = String.format(DEFAULT_FILE, gender.getPlaceholder().toLowerCase(), ethnicity.getPlaceholder());
        return load(filePath);
    }

    
}
