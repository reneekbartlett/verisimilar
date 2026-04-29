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

        // TODO:  Add b2b, disposable
        Map<String, Double> b2c = loadTypeDataset("b2c", key);
        //Map<String, Double> b2b = loadTypeDataset("b2b", key);
        Map<String, Double> edu = loadTypeDataset("edu", key);
        Map<String, Double> gov = loadTypeDataset("gov", key);
        //Map<String, Double> disposable = loadTypeDataset("disposable", key);

        return new DomainDatasetResult(
                b2c, 
                HashMap.newHashMap(1), // b2b
                edu, 
                gov, 
                HashMap.newHashMap(1) // disposable
        );
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
        return load(datasetPath);
    }
}
