package com.reneekbartlett.verisimilar.core.datasets.resolver;

import com.reneekbartlett.verisimilar.core.datasets.DatasetSource;
import com.reneekbartlett.verisimilar.core.datasets.key.UsernameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.UsernameDatasetResult;

public class UsernameDatasetResolver extends AbstractDatasetResolver<UsernameDatasetKey, UsernameDatasetResult> {

    //private static final String KEYWORDS_FILE_FORMAT = "datasets/cfg_username_keywords_%s.csv";

    //private String filePathFormat;

    public UsernameDatasetResolver(DatasetSource<UsernameDatasetKey, UsernameDatasetResult> source) {
        super(source);
    }

    //public UsernameDatasetResolver(ResourceLoaderUtil loader, @NonNull String filePathFormat) {
    //    super(loader);
    //    this.filePathFormat = filePathFormat;
    //}

    //public UsernameDatasetResolver(ResourceLoaderUtil loader) {
    //    this(loader, KEYWORDS_FILE_FORMAT);
    //}

//    @Override
//    public UsernameDatasetResult loadForKey(UsernameDatasetKey key) {
//        String[] keywords = loadKeywordValues(key);
//        Map<String, Double> usernameWeights = new HashMap<>();
//        Double w = 1.000;
//        for(String k : keywords) {
//            usernameWeights.put(k, w);
//        }
//        return new UsernameDatasetResult(usernameWeights);
//    }

    @Override
    public Class<UsernameDatasetKey> keyType(){
        return UsernameDatasetKey.class;
    }

    @Override
    public Class<UsernameDatasetResult> resultType() {
        return UsernameDatasetResult.class;
    }

//    private String[] loadKeywordValues(UsernameDatasetKey key) {
//        String filePath = String.format(filePathFormat, "ALL");
//        return loader.loadStringArray(filePath);
//    }
}
