package com.reneekbartlett.verisimilar.core.datasets;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.DomainDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.DomainDatasetResult;

public class DomainFileMapper extends AbstractFileDatasetMapper<DomainDatasetKey, DomainDatasetResult> {

    private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_domain_%s_2025.csv";

    private final String filePathFormat;

    public DomainFileMapper(String filePathFormat) {
        this.filePathFormat = DEFAULT_FILE_FORMAT;
    }

    public DomainFileMapper() {
        this(DEFAULT_FILE_FORMAT);
    }

    @Override
    public DomainDatasetResult loadFromFile(DomainDatasetKey key, ResourceLoaderUtil loader) {
        if(key != null) {
            //TODO:  iterate DomainDatasetKey.domainTypes[]
        }

        // TODO:  Add disposable
        Map<String, Double> b2c = loadTypeDataset(loader, "b2c", key);
        Map<String, Double> b2b = loadTypeDataset(loader, "b2b", key);
        Map<String, Double> edu = loadTypeDataset(loader, "edu", key);
        Map<String, Double> gov = loadTypeDataset(loader, "gov", key);
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

    private Map<String, Double> loadTypeDataset(ResourceLoaderUtil loader, String type, DomainDatasetKey key) {
        String datasetPath = String.format(filePathFormat, type);
        return load(loader, datasetPath);
    }

    @Override
    protected String resolvePath(DomainDatasetKey key) {
        // TODO Auto-generated method stub
        return null;
    }

}
