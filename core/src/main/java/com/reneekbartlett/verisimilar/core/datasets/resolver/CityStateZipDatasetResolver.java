package com.reneekbartlett.verisimilar.core.datasets.resolver;

import com.reneekbartlett.verisimilar.core.datasets.DatasetSource;
import com.reneekbartlett.verisimilar.core.datasets.key.CityStateZipDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.CityStateZipDatasetResult;

public class CityStateZipDatasetResolver extends AbstractDatasetResolver<CityStateZipDatasetKey, CityStateZipDatasetResult> {

    public CityStateZipDatasetResolver(DatasetSource<CityStateZipDatasetKey, CityStateZipDatasetResult> source) {
        super(source);
    }

    @Override
    public Class<CityStateZipDatasetKey> keyType() {
        return CityStateZipDatasetKey.class;
    }

    @Override
    public Class<CityStateZipDatasetResult> resultType() {
        return CityStateZipDatasetResult.class;
    }

}
