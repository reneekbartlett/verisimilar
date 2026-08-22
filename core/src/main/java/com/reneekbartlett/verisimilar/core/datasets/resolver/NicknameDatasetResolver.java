package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.NicknameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.NicknameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.Decade;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.selector.engine.NicknameSelectionEngine.NameKey;

public class NicknameDatasetResolver extends AbstractDatasetResolver<NicknameDatasetKey, NicknameDatasetResult> {

    private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_nickname_%s_%s.csv";

    private EnumSet<GenderIdentity> genderIdentities;
    private EnumSet<Ethnicity> ethnicities;

    private final String filePathFormat;

    public NicknameDatasetResolver(ResourceLoaderUtil loader, String filePathFormat) {
        super(loader);
        this.filePathFormat = filePathFormat;
        //this.genderIdentities = GenderIdentity.defaults();
        //this.ethnicities = Ethnicity.defaultDatasets();
    }

    public NicknameDatasetResolver(ResourceLoaderUtil loader) {
        this(loader, DEFAULT_FILE_FORMAT);
    }

    @Override
    public NicknameDatasetResult loadForKey(NicknameDatasetKey key) {
        this.genderIdentities = key.genders();
        this.ethnicities = EnumSet.noneOf(Ethnicity.class); //Ethnicity.defaultDatasets();

        Map<NameKey, Map<String, Double>> datasets = HashMap.newHashMap(0);

        // Load both datasets (male + female) for the given ethnicity
        for(GenderIdentity genderIdentity : genderIdentities) {
            datasets.put(new NameKey(genderIdentity, Ethnicity.UNKNOWN), loadGenderDataset(genderIdentity));
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

    private Map<String, Double> loadGenderDataset(GenderIdentity gender) {
        String genderStr = gender.getPlaceholder().toLowerCase();
        String fallback = String.format(filePathFormat, genderStr, "ALL");
        LOGGER.debug("{}", fallback);
        return load(fallback);
    }

    @SuppressWarnings("unused")
    //private Map<String, Double> loadEthnicityDataset(GenderIdentity gender, Ethnicity ethnicity) {
    //    String filePath = String.format(filePathFormat, gender.name().toLowerCase(), ethnicity.getPlaceholder());
    //    return load(filePath);
    //}

    @Override
    public Class<NicknameDatasetKey> keyType() {
        return NicknameDatasetKey.class;
    }

    @Override
    public Class<NicknameDatasetResult> resultType() {
        return NicknameDatasetResult.class;
    }
}
