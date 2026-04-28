package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.UsernameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.UsernameDatasetResult;

public class UsernameDatasetResolver extends AbstractDatasetResolver<UsernameDatasetKey, UsernameDatasetResult> {

    private static final String KEYWORDS_FILE = "datasets/cfg_username_keywords_ALL.csv";

    public UsernameDatasetResolver(ResourceLoaderUtil loader) {
        super(loader);
        //this.allTemplates = allTemplates;
    }

    @Override
    public UsernameDatasetResult loadForKey(UsernameDatasetKey key) {
        String[] keywords = loadKeywordValues(key);
        Map<String, Double> usernameWeights = new HashMap<>();
        Double w = 1.000;
        for(String k : keywords) {
            usernameWeights.put(k, w);
        }
        //return new UsernameDatasetResult(usernameWeights, allTemplates);
        return new UsernameDatasetResult(usernameWeights);
    }

    @Override
    public Class<UsernameDatasetKey> keyType(){
        return UsernameDatasetKey.class;
    }

    @Override
    public Class<UsernameDatasetResult> resultType() {
        return UsernameDatasetResult.class;
    }

    private String[] loadKeywordValues(UsernameDatasetKey key) {
        return loader.loadStringArray(KEYWORDS_FILE);
    }
}
