package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.DomainDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.DomainDatasetResult;

/***
 * 
 */
public class DomainDatasetResolver extends AbstractDatasetResolver<DomainDatasetKey, DomainDatasetResult> {

    private static final String DEFAULT_FILE = "datasets/cfg_domains_%s_2025.csv";

    public DomainDatasetResolver(ResourceLoaderUtil loader) {
        super(loader);
    }

    @Override
    public DomainDatasetResult loadForKey(DomainDatasetKey key) {
        if(key != null) {
            //TODO:  iterate DomainDatasetKey.domainTypes[]
        }

        Map<String, Double> b2c = loadTypeDataset("b2c", key);
        //Map<String, Double> b2b = loadTypeDataset("b2b", key);
        Map<String, Double> edu = loadTypeDataset("edu", key);
        Map<String, Double> gov = loadTypeDataset("gov", key);
        //Map<String, Double> disposable = loadTypeDataset("disposable", key);
        //return new DomainDatasetResult(b2c, b2b, disposable, edu, gov);

        //String[] templates = new String[0];

        //LOGGER.debug("loadForKey - b2c:{}, edu:{}, gov:{}", b2c.size(), edu.size(), gov.size());

        return new DomainDatasetResult(b2c, HashMap.newHashMap(1), edu, gov);
    }

    @Override
    public Class<DomainDatasetKey> keyType() {
        return DomainDatasetKey.class;
    }

    @Override
    public Class<DomainDatasetResult> resultType() {
        return DomainDatasetResult.class;
    }

    private Map<String, Double> loadTypeDataset(String type, DomainDatasetKey key) {
        String datasetPath = String.format(DEFAULT_FILE, type);
        //LOGGER.debug("Default Path: " + datasetPath);
        return load(datasetPath);
    }
}
