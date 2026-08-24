package com.reneekbartlett.verisimilar.core.datasets;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.KeywordDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.KeywordDatasetResult;
import com.reneekbartlett.verisimilar.core.model.TemplateField;

public class KeywordFileMapper extends AbstractFileDatasetMapper<KeywordDatasetKey, KeywordDatasetResult> {

    public static final TemplateField DEFAULT_RETURN_FIELD = TemplateField.KEYWORD1;
    private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_username_keywords_%s.csv";
    //private static final String KEYWORDS_FILE_FORMAT = "datasets/cfg_username_keywords_%s.csv";

    private final String filePathFormat;

    public KeywordFileMapper(String filePathFormat) {
        this.filePathFormat = DEFAULT_FILE_FORMAT;
    }

    public KeywordFileMapper() {
        this(DEFAULT_FILE_FORMAT);
    }

    @Override
    public KeywordDatasetResult loadFromFile(KeywordDatasetKey key, ResourceLoaderUtil loader) {
        String[] keywords = loadKeywordValues(loader, key);
        Map<String, Double> keywordWeights = new HashMap<>();
        Double w = 1.000;
        for(String k : keywords) {
            keywordWeights.put(k, w);
        }
        return new KeywordDatasetResult(keywordWeights);
    }

    private String[] loadKeywordValues(ResourceLoaderUtil loader, KeywordDatasetKey key) {
        String filePath = String.format(filePathFormat, "ALL");
        return loader.loadStringArray(filePath);
    }

    @Override
    protected String resolvePath(KeywordDatasetKey key) {
        // TODO Auto-generated method stub
        return null;
    }

}
