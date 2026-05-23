package com.reneekbartlett.verisimilar.core.datasets.resolver;

import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.AddressTwoDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
import com.reneekbartlett.verisimilar.core.datasets.result.AddressTwoDatasetResult;

/***
 * cfg_postaladdress_address2_unit_ALL.csv
 */
public class AddressTwoDatasetResolver extends AbstractDatasetResolver<AddressTwoDatasetKey, AddressTwoDatasetResult> {

    private static final String DEFAULT_FILE_FORMAT = "datasets/cfg_postaladdress_address2_unit_%s.csv";

    public AddressTwoDatasetResolver(ResourceLoaderUtil loader) {
        super(loader);
    }

    @Override
    public AddressTwoDatasetResult loadForKey(AddressTwoDatasetKey key) {
        Map<String, Double> all = loadDataset(key);
        return new AddressTwoDatasetResult(all);
    }

    @Override
    public Class<AddressTwoDatasetKey> keyType() {
        return AddressTwoDatasetKey.class;
    }

    @Override
    public Class<AddressTwoDatasetResult> resultType() {
        return AddressTwoDatasetResult.class;
    }

    private Map<String, Double> loadDataset(AddressTwoDatasetKey key) {
        String filePath = String.format(DEFAULT_FILE_FORMAT, "ALL");
        return loader.loadWeightedMap(filePath);
    }
}
