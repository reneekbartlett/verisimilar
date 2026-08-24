package com.reneekbartlett.verisimilar.core.datasets;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.reneekbartlett.verisimilar.core.datasets.key.FirstNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.FirstNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.Decade;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.selector.engine.FirstNameSelectionEngine.NameKey;

public class FirstNameFileMapper extends AbstractFileDatasetMapper<FirstNameDatasetKey, FirstNameDatasetResult> {

    private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_full_name_first_name_%s_%s.csv";

    private final String filePathFormat;

    public FirstNameFileMapper(String filePathFormat) {
        this.filePathFormat = DEFAULT_FILE_FORMAT;
    }

    public FirstNameFileMapper() {
        this(DEFAULT_FILE_FORMAT);
    }

    @Override
    public FirstNameDatasetResult loadFromFile(FirstNameDatasetKey key, ResourceLoaderUtil loader) {

        Set<GenderIdentity> genderIdentities = GenderIdentity.defaults();
        Set<Ethnicity> ethnicities = Ethnicity.defaultDatasets();
        Set<Decade> decades = Decade.defaultDatasets();

        Map<NameKey, Map<String, Double>> datasets = HashMap.newHashMap(0);

        // Load datasets that have ethnicity data (male + female) for the given ethnicity
        for(GenderIdentity genderIdentity : genderIdentities) {
            datasets.put(new NameKey(genderIdentity, Ethnicity.UNKNOWN, Decade.ALL), 
                    loadGenderDataset(loader, genderIdentity));
        }

        // Load Unisex names, without ethnicity for now.
        if(GenderIdentity.defaultDatasets().contains(GenderIdentity.GENDER_UNSPECIFIED)) {
            datasets.put(new NameKey(GenderIdentity.GENDER_UNSPECIFIED, Ethnicity.UNKNOWN, Decade.ALL), 
                    loadGenderDataset(loader, GenderIdentity.GENDER_UNSPECIFIED));
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
                    datasets.put(new NameKey(gender, ethnicity, Decade.ALL), 
                            loadEthnicityDataset(loader, gender, ethnicity));
                });
            }
        }

        return new FirstNameDatasetResult(datasets);
    }

    private Map<String, Double> loadGenderDataset(ResourceLoaderUtil loader, GenderIdentity gender) {
        String filePath = String.format(filePathFormat, gender.getPlaceholder().toLowerCase(), "ALL");
        return load(loader,filePath);
    }

    private Map<String, Double> loadEthnicityDataset(ResourceLoaderUtil loader, GenderIdentity gender, Ethnicity ethnicity) {
        String filePath = String.format(filePathFormat, gender.getPlaceholder().toLowerCase(), ethnicity.getPlaceholder());
        return load(loader,filePath);
    }

    @Override
    protected String resolvePath(FirstNameDatasetKey key) {
        // TODO Auto-generated method stub
        return null;
    }
}
