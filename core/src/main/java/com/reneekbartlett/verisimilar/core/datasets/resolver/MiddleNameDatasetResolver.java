package com.reneekbartlett.verisimilar.core.datasets.resolver;

import com.reneekbartlett.verisimilar.core.datasets.DatasetSource;
import com.reneekbartlett.verisimilar.core.datasets.key.MiddleNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.MiddleNameDatasetResult;

/***
 * For resolving paths and retrieving MIDDLE_NAME values from dataset files
 */
public class MiddleNameDatasetResolver extends AbstractDatasetResolver<MiddleNameDatasetKey, MiddleNameDatasetResult> {

    //private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_full_name_middle_name_%s_%s.csv";

    public MiddleNameDatasetResolver(DatasetSource<MiddleNameDatasetKey, MiddleNameDatasetResult> source) {
        super(source);
    }

    //public MiddleNameDatasetResolver(ResourceLoaderUtil loader, String filePathFormat) {
    //    super(loader);
    //}

    //public MiddleNameDatasetResolver(ResourceLoaderUtil loader) {
    //    this(loader, DEFAULT_FILE_FORMAT);
    //}

//    @Override
//    public MiddleNameDatasetResult loadForKey(MiddleNameDatasetKey key) {
//        Map<NameKey, Map<String, Double>> datasets = HashMap.newHashMap(0);
//
//        for(GenderIdentity genderIdentity : genderIdentities) {
//            datasets.put(new NameKey(genderIdentity, Ethnicity.UNKNOWN), loadGenderDataset(genderIdentity));
//        }
//
//        // TODO: Add Ethnicity Datasets
//        // Use culture data on whether or not to populate based on ethnicity
//        for(Ethnicity ethnicity : ethnicities) {
//            if(ethnicity != Ethnicity.UNKNOWN) {
//                // TODO
//                //for(GenderIdentity genderIdentity : genderIdentities) {
//                //    //datasets.put(new NameKey(genderIdentity, Ethnicity.UNKNOWN), loadEthnicityDataset(genderIdentity, ethnicity));
//                //}
//            }
//        }
//
//        return new MiddleNameDatasetResult(datasets);
//    }
//
//    private Map<String, Double> loadGenderDataset(GenderIdentity gender) {
//        String filePath = String.format(filePathFormat, gender.name().toLowerCase(), "ALL");
//        return load(filePath);
//    }
//
//    @SuppressWarnings("unused")
//    private Map<String, Double> loadEthnicityDataset(GenderIdentity gender, Ethnicity ethnicity) {
//        String filePath = String.format(filePathFormat, gender.name().toLowerCase(), ethnicity.getPlaceholder());
//        if(!exists(filePath)) {
//            // TODO:  Warn/add placeholders
//            return HashMap.newHashMap(0);
//        }
//        return load(filePath);
//    }

    @Override
    public Class<MiddleNameDatasetKey> keyType() {
        return MiddleNameDatasetKey.class;
    }

    @Override
    public Class<MiddleNameDatasetResult> resultType() {
        return MiddleNameDatasetResult.class;
    }
}
