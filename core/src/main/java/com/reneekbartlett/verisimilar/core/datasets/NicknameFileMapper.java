package com.reneekbartlett.verisimilar.core.datasets;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.NicknameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.NicknameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.selector.engine.NicknameSelectionEngine.NameKey;

public class NicknameFileMapper extends AbstractFileDatasetMapper<NicknameDatasetKey, NicknameDatasetResult> {

    private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_nickname_%s_%s.csv";

    private final String filePathFormat;

    public NicknameFileMapper(String filePathFormat) {
        this.filePathFormat = DEFAULT_FILE_FORMAT;
    }

    public NicknameFileMapper() {
        this(DEFAULT_FILE_FORMAT);
    }

    @Override
    public NicknameDatasetResult loadFromFile(NicknameDatasetKey key, ResourceLoaderUtil loader) {

        EnumSet<GenderIdentity> genderIdentities = key.genders();
        EnumSet<Ethnicity> ethnicities = EnumSet.noneOf(Ethnicity.class); //Ethnicity.defaultDatasets();

        Map<NameKey, Map<String, Double>> datasets = HashMap.newHashMap(0);

        // Load both datasets (male + female) for the given ethnicity
        for(GenderIdentity genderIdentity : genderIdentities) {
            datasets.put(new NameKey(genderIdentity, Ethnicity.UNKNOWN), loadGenderDataset(loader, genderIdentity));
        }

        for(Ethnicity ethnicity : ethnicities) {
            if(ethnicity != Ethnicity.UNKNOWN) {
                // TODO:  Add ethnicity files.
                //for(GenderIdentity genderIdentity : genderIdentities) {
                    //NameKey nameKey = new NameKey(genderIdentity, ethnicity);
                    //datasets.put(nameKey, loadEthnicityDataset(genderIdentity, ethnicity));
                //}
            }
        }

        return new NicknameDatasetResult(datasets);
    }

    private Map<String, Double> loadGenderDataset(ResourceLoaderUtil loader, GenderIdentity gender) {
        String genderStr = gender.getPlaceholder().toLowerCase();
        String fallback = String.format(filePathFormat, genderStr, "ALL");
        //LOGGER.debug("{}", fallback);
        return load(loader, fallback);
    }

    @Override
    protected String resolvePath(NicknameDatasetKey key) {
        // TODO Auto-generated method stub
        return null;
    }

}
