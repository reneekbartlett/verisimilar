package com.reneekbartlett.verisimilar.core.datasets.resolver;

import com.reneekbartlett.verisimilar.core.datasets.DatasetSource;
import com.reneekbartlett.verisimilar.core.datasets.key.StreetNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.StreetNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.TemplateField;

/***
 * For resolving and retrieving StreetName values from dataset files
 * List<String> cfg_postal_address_address1_street_name_ALL.csv
 */
public class StreetNameDatasetResolver extends AbstractDatasetResolver<StreetNameDatasetKey, StreetNameDatasetResult> {

    public static final TemplateField DEFAULT_RETURN_FIELD = TemplateField.STREET_NAME;

    public StreetNameDatasetResolver(DatasetSource<StreetNameDatasetKey, StreetNameDatasetResult> source) {
        super(source);
    }

    @Override
    public Class<StreetNameDatasetKey> keyType() {
        return StreetNameDatasetKey.class;
    }

    @Override
    public Class<StreetNameDatasetResult> resultType() {
        return StreetNameDatasetResult.class;
    }
}
