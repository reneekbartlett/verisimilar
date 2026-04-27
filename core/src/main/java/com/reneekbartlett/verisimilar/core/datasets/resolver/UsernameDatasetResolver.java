package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.reneekbartlett.verisimilar.core.datasets.key.UsernameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.UsernameDatasetResult;

// TODO:  Rename KeywordDatasetResolver, etc?
public class UsernameDatasetResolver extends AbstractDatasetResolver<UsernameDatasetKey, UsernameDatasetResult> {
    //private static final Logger LOGGER = LoggerFactory.getLogger(UsernameDatasetResolver.class);
    private static final String KEYWORDS_FILE = "datasets/cfg_username_keywords_ALL.csv";
    private final Set<String> allTemplates;

    public UsernameDatasetResolver(ResourceLoaderUtil loader, Set<String> allTemplates) {
        super(loader);
        this.allTemplates = allTemplates;
    }

    @Override
    public UsernameDatasetResult loadForKey(UsernameDatasetKey key) {
        String[] keywords = loadKeywordValues(key);
        Map<String, Double> usernameWeights = new HashMap<>();
        Double w = 1.000;
        for(String k : keywords) {
            usernameWeights.put(k, w);
        }
        return new UsernameDatasetResult(usernameWeights, allTemplates);
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
        //LOGGER.debug("Loading keywords: " + KEYWORDS_FILE);
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
