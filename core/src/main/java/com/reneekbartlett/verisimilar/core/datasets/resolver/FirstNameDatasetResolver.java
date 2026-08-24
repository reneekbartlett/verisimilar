package com.reneekbartlett.verisimilar.core.datasets.resolver;

import com.reneekbartlett.verisimilar.core.datasets.DatasetSource;
import com.reneekbartlett.verisimilar.core.datasets.key.FirstNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.FirstNameDatasetResult;

/***
 *  For resolving paths and retrieving FIRST_NAME values from dataset files
 */
public class FirstNameDatasetResolver extends AbstractDatasetResolver<FirstNameDatasetKey, FirstNameDatasetResult> {

    public FirstNameDatasetResolver(DatasetSource<FirstNameDatasetKey, FirstNameDatasetResult> source) {
        super(source);
    }

    @Override
    public Class<FirstNameDatasetKey> keyType() {
        return FirstNameDatasetKey.class;
    }

    @Override
    public Class<FirstNameDatasetResult> resultType() {
        return FirstNameDatasetResult.class;
    }
}
