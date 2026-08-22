package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.DomainDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.DomainDatasetResult;

/***
 * For resolving and retrieving DOMAIN values from dataset files
 */
public class DomainDatasetResolver extends AbstractDatasetResolver<DomainDatasetKey, DomainDatasetResult> {

    private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_domain_%s_2025.csv";

    private final String filePathFormat;

    public DomainDatasetResolver(ResourceLoaderUtil loader, String filePathFormat) {
        super(loader);
        this.filePathFormat = filePathFormat;
    }

    public DomainDatasetResolver(ResourceLoaderUtil loader) {
        this(loader, DEFAULT_FILE_FORMAT);
    }

    @Override
    public DomainDatasetResult loadForKey(DomainDatasetKey key) {
        if(key != null) {
            //TODO:  iterate DomainDatasetKey.domainTypes[]
        }

        // TODO:  Add disposable
        Map<String, Double> b2c = loadTypeDataset("b2c", key);
        Map<String, Double> b2b = loadTypeDataset("b2b", key);
        Map<String, Double> edu = loadTypeDataset("edu", key);
        Map<String, Double> gov = loadTypeDataset("gov", key);
        //Map<String, Double> disposable = loadTypeDataset("disposable", key);

        // Loading temp map of disposable domains for now.
        Map<String, Double> disposable = new HashMap<>();
        disposable.put("mailinator.com", 1.0000);
        disposable.put("guerrillamail.com", 1.0000);
        disposable.put("10minutemail.com", 1.0000);
        disposable.put("yopmail.com", 1.0000);
        disposable.put("temp-mail.org", 1.0000);
        disposable.put("dropmail.me", 1.0000);
        disposable.put("onetimeemail.net", 1.0000);

        return new DomainDatasetResult(
                b2c, 
                b2b,
                edu, 
                gov, 
                disposable
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
        String datasetPath = String.format(filePathFormat, type);
        return load(datasetPath);
    }
}
