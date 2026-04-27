package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.KeywordDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.KeywordDatasetResult;

public class KeywordDatasetResolver extends AbstractDatasetResolver<KeywordDatasetKey, KeywordDatasetResult> {
    private static final String KEYWORDS_FILE = "datasets/cfg_username_keywords_ALL.csv";

    public KeywordDatasetResolver(ResourceLoaderUtil loader) {
        super(loader);
    }

    @Override
    public KeywordDatasetResult loadForKey(KeywordDatasetKey key) {
        String[] keywords = loadKeywordValues(key);
        Map<String, Double> keywordWeights = new HashMap<>();
        Double w = 1.000;
        for(String k : keywords) {
            keywordWeights.put(k, w);
        }
        return new KeywordDatasetResult(keywordWeights);
    }

    @Override
    public Class<KeywordDatasetKey> keyType(){
        return KeywordDatasetKey.class;
    }

    @Override
    public Class<KeywordDatasetResult> resultType() {
        return KeywordDatasetResult.class;
    }

    private String[] loadKeywordValues(KeywordDatasetKey key) {
        return loader.loadStringArray(KEYWORDS_FILE);
    }

    // TODO:  i.e renee_nyc@gmail.com
    //private static final String[] KEYWORDS_LOCATION = { "NYC" };

    //private static final String[] KEYWORDS_POPULAR_NOUN = {
    //    "SURFER", "SKATER", "GAMER", "KID", "CODER", "PUPPY", "DOG", "CAT",
    //    "COMPUTER", "ARTIST"
    //};

    //private static final String[] KEYWORDS_POPULAR_DESC = {
    //    "DRAWS", "KID", "COOL", "AWESOME", "LOVER"
    //};

    //private static final String[] KEYWORDS_POPULAR_FEMALE = {
    //    "GIRL", "CHICK", "CHICA", "GIRLY", "SWEETIE", "CUTIE", "MOM", "NANA"
    //};

    //private static final String[] KEYWORDS_POPULAR_MALE = {
    //    "BOY", "DUDE", "BRO", "DAD", "PAPA"
    //};
}
