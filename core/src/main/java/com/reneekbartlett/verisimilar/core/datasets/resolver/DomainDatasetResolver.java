package com.reneekbartlett.verisimilar.core.datasets.resolver;

import com.reneekbartlett.verisimilar.core.datasets.DatasetSource;
import com.reneekbartlett.verisimilar.core.datasets.key.DomainDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.DomainDatasetResult;

/***
 * For resolving and retrieving DOMAIN values from dataset files
 */
public class DomainDatasetResolver extends AbstractDatasetResolver<DomainDatasetKey, DomainDatasetResult> {

    public DomainDatasetResolver(DatasetSource<DomainDatasetKey, DomainDatasetResult> source) {
        super(source);
    }

    @Override
    public Class<DomainDatasetKey> keyType() {
        return DomainDatasetKey.class;
    }

    @Override
    public Class<DomainDatasetResult> resultType() {
        return DomainDatasetResult.class;
    }
}
