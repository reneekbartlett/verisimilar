package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.CityStateZipDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.CityStateZipDatasetResult;
import com.reneekbartlett.verisimilar.core.model.CityStateZip;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.datasets.resolver.CityStateZipDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.UniformSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class CityStateZipSelectionEngine extends AbstractSelectionEngine<CityStateZipDatasetKey,CityStateZipDatasetResult> {
    /***
     * Default file has no weight, so use Uniform [AK,WILLOW$AK$99688]
     */
    private static final SelectorStrategy<String> DEFAULT_SELECTOR_STRATEGY = new UniformSelectorStrategy<>();

    private Map<NameKey, RandomSelector<String>> selectorsByNameKey;

    public record NameKey() {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(0).append("dataset$citystatezip");
            return sb.toString();
        }
    }

    public CityStateZipSelectionEngine(DatasetResolverRegistry resolvers) {
        super(resolvers, DEFAULT_SELECTOR_STRATEGY);
    }

    public CityStateZipSelectionEngine(DatasetResolverRegistry resolvers, SelectorStrategy<String> strategy) {
        super(resolvers, strategy);
    }

    public CityStateZipSelectionEngine(CityStateZipDatasetResolver resolver) {
        super(resolver, DEFAULT_SELECTOR_STRATEGY);
    }

    @Override
    protected void setup() {
        CityStateZipDatasetResult result = datasetResolver().resolve(CityStateZipDatasetKey.defaults());
        this.selectorsByNameKey = HashMap.newHashMap(result.datasets().size());
        result.datasets().forEach((nameKey, map) -> {
            RandomSelector<String> selector = strategy.buildSelector(map, field());
            selectorsByNameKey.put(nameKey, selector);
        });
    }

    @Override
    public String select(CityStateZipDatasetKey key, SelectionFilter filter) {
        // If filter has all 3, use user inputs.
        if(filter != null && filter.equalToMap().containsKey(TemplateField.STATE) 
                && filter.equalToMap().containsKey(TemplateField.CITY)
                && filter.equalToMap().containsKey(TemplateField.ZIP_CODE)
        ) {
            return new CityStateZip(
                    filter.equalToMap().get(TemplateField.CITY),
                    filter.equalToMap().get(TemplateField.STATE),
                    filter.equalToMap().get(TemplateField.ZIP_CODE)
            ).toString();
        }

        // There are currently no CityStateZipSelectionEngine.NameKey parameters, so just get default.
        NameKey nameKey = new NameKey();
        RandomSelector<String> selector = selectorsByNameKey.get(nameKey);
        if (selector == null) {
            throw new IllegalStateException("No selector registered for " + nameKey);
        }
        if(filter != null && !filter.isEmpty()) {
            // If filter has all 3, use user inputs.
            if(filter.equalToMap().containsKey(TemplateField.CITY)) {
                // TODO: Add Filtering / custom predicate?
                filter = filter.toBuilder().startsWith(filter.city().get(),TemplateField.CITY).build(); 
            }
            selector.setFilter(filter);
        }
        return selector.select();
    }

    @Override
    public CityStateZipDatasetKey defaultKey() {
        return CityStateZipDatasetKey.defaults();
    }

    @Override
    public Class<CityStateZipDatasetKey> keyType() {
        return CityStateZipDatasetKey.class;
    }

    @Override
    public Class<CityStateZipDatasetResult> resultType() {
        return CityStateZipDatasetResult.class;
    }

    public TemplateField field() {
        // TODO Change to ZIP/STATE?  Concat?
        return TemplateField.CITY;
    }
}
