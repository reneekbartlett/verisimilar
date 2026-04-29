package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.reneekbartlett.verisimilar.core.datasets.key.NicknameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.NicknameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.selector.engine.NicknameSelectionEngine.NameKey;

public class NicknameDatasetResolver extends AbstractDatasetResolver<NicknameDatasetKey, NicknameDatasetResult> {

    private static final String DEFAULT_FILE = "datasets/cfg_nicknames_%s_%s.csv";

    private final Set<GenderIdentity> genderIdentities;
    private final Set<Ethnicity> ethnicities;

    public NicknameDatasetResolver(ResourceLoaderUtil loader) {
        super(loader);
        this.genderIdentities = GenderIdentity.defaults();
        this.ethnicities = Ethnicity.defaultDatasets();
    }

    @Override
    public NicknameDatasetResult loadForKey(NicknameDatasetKey key) {
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
        String genderStr = gender.name().toLowerCase();
        String fallback = String.format(DEFAULT_FILE, genderStr, "ALL");

        return load(fallback);
    }

    @SuppressWarnings("unused")
    private Map<String, Double> loadEthnicityDataset(GenderIdentity gender, Ethnicity ethnicity) {
        String filePath = String.format(DEFAULT_FILE, gender.name().toLowerCase(), ethnicity.getPlaceholder());
        return load(filePath);
    }

    @Override
    public Class<NicknameDatasetKey> keyType() {
        return NicknameDatasetKey.class;
    }

    @Override
    public Class<NicknameDatasetResult> resultType() {
        return NicknameDatasetResult.class;
    }
}
