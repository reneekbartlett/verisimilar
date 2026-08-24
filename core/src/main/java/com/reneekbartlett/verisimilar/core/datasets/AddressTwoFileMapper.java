package com.reneekbartlett.verisimilar.core.datasets;

import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.AddressTwoDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.AddressTwoDatasetResult;

public class AddressTwoFileMapper extends AbstractFileDatasetMapper<AddressTwoDatasetKey, AddressTwoDatasetResult> {

    private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_postal_address_address2_unit_type_%s.csv";

    private final String filePathFormat;

    public AddressTwoFileMapper(String filePathFormat) {
        this.filePathFormat = DEFAULT_FILE_FORMAT;
    }

    public AddressTwoFileMapper() {
        this(DEFAULT_FILE_FORMAT);
    }

    @Override
    public AddressTwoDatasetResult loadFromFile(AddressTwoDatasetKey key, ResourceLoaderUtil loader) {
        Map<String, Double> all = loadDataset(loader, key);
        return new AddressTwoDatasetResult(all);
    }

    private Map<String, Double> loadDataset(ResourceLoaderUtil loader, AddressTwoDatasetKey key) {
        String filePath = String.format(filePathFormat, "ALL");
        return loader.loadWeightedMap(filePath);
    }

    @Override
    protected String resolvePath(AddressTwoDatasetKey key) {
        // TODO Auto-generated method stub
        return null;
    }

}
