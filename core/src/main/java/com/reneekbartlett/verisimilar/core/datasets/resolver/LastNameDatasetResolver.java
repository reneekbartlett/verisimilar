package com.reneekbartlett.verisimilar.core.datasets.resolver;

import com.reneekbartlett.verisimilar.core.datasets.DatasetSource;
import com.reneekbartlett.verisimilar.core.datasets.key.LastNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.LastNameDatasetResult;

/***
 * 
 */
public class LastNameDatasetResolver extends AbstractDatasetResolver<LastNameDatasetKey, LastNameDatasetResult> {

    // "datasets/cfg_fullname_last_%s.csv"
    //private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_full_name_last_name_%s.csv";

    //private String filePathFormat;
    //private EnumSet<Ethnicity> ethnicities;

    public LastNameDatasetResolver(DatasetSource<LastNameDatasetKey, LastNameDatasetResult> source) {
        super(source);
    }

    //public LastNameDatasetResolver(ResourceLoaderUtil loader, String filePathFormat) {
    //    super(loader);
    //    this.filePathFormat = filePathFormat;
    //    this.ethnicities = Ethnicity.defaultDatasets();
    //}

    //public LastNameDatasetResolver(ResourceLoaderUtil loader) {
    //    this(loader, DEFAULT_FILE_FORMAT);
    //}

    /***
     * Function passed to DatasetCache. 
     */
//    @Override
//    public LastNameDatasetResult loadForKey(LastNameDatasetKey key) {
//        //LOGGER.debug("loadForKey - key:{}", key);
//
//        EnumSet<Ethnicity> ethnicities;
//        if(!key.ethnicities().isEmpty())
//            ethnicities = key.ethnicities();
//        else {
//            LOGGER.warn("Empty dataset.");
//            ethnicities = this.ethnicities;
//        }
//
//        Map<NameKey, Map<String, Double>> datasets = HashMap.newHashMap(ethnicities.size());
//        for(Ethnicity ethnicity : ethnicities) {
//            Map<String, Double> map = loadDataset(ethnicity);
//            datasets.put(new NameKey(ethnicity), map);
//        }
//
//        return new LastNameDatasetResult(datasets);
//    }
//
//    private Map<String, Double> loadDataset(Ethnicity ethnicity) {
//        String filePath = String.format(filePathFormat, ethnicity.getPlaceholder());
//        return load(filePath);
//    }

    @Override
    public Class<LastNameDatasetKey> keyType() {
        return LastNameDatasetKey.class;
    }

    @Override
    public Class<LastNameDatasetResult> resultType() {
        return LastNameDatasetResult.class;
    }
}
