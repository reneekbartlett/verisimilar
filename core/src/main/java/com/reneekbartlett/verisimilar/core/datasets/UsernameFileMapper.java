package com.reneekbartlett.verisimilar.core.datasets;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.UsernameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.UsernameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.TemplateField;

public class UsernameFileMapper extends AbstractFileDatasetMapper<UsernameDatasetKey, UsernameDatasetResult> {

    public static final TemplateField DEFAULT_RETURN_FIELD = TemplateField.USERNAME;
    private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_username_keywords_%s.csv";
    //private static final String KEYWORDS_FILE_FORMAT = "datasets/cfg_username_keywords_%s.csv";

    private final String filePathFormat;

    public UsernameFileMapper(String filePathFormat) {
        this.filePathFormat = DEFAULT_FILE_FORMAT;
    }

    public UsernameFileMapper() {
        this(DEFAULT_FILE_FORMAT);
    }

    @Override
    public UsernameDatasetResult loadFromFile(UsernameDatasetKey key, ResourceLoaderUtil loader) {
        String[] keywords = loadKeywordValues(loader, key);
        Map<String, Double> usernameWeights = new HashMap<>();
        Double w = 1.000;
        for(String k : keywords) {
            usernameWeights.put(k, w);
        }
        return new UsernameDatasetResult(usernameWeights);
    }

    private String[] loadKeywordValues(ResourceLoaderUtil loader, UsernameDatasetKey key) {
        String filePath = String.format(filePathFormat, "ALL");
        return loader.loadStringArray(filePath);
    }

    @Override
    protected String resolvePath(UsernameDatasetKey key) {
        // TODO Auto-generated method stub
        return null;
    }

}
