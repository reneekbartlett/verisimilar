package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.reneekbartlett.verisimilar.core.datasets.key.AreaCodeDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.AreaCodeDatasetResult;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.UniformSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

/***
 * Map<String, String[]> AREACODES_BY_STATE = ResourceMapLoader.loadArrayMap("/cfg_phonenumber_areacode_bystate_ALL.csv");
 * AZ,480|520|602|623|928
 */
public class AreaCodeSelectionEngine extends AbstractSelectionEngine<AreaCodeDatasetKey, AreaCodeDatasetResult> {
    private static final SelectorStrategy<String> DEFAULT_SELECTOR_STRATEGY = new WeightedSelectorStrategy<>();
    private final Map<USState, Double> usStateMap;
    private final RandomSelector<USState> usStateSelector;
    private Map<NameKey, RandomSelector<String>> datasetsByNameKey;

    //  For meta-selection (which selector to use at runtime)
    public record NameKey(String usStateAbbr) {
        public NameKey() {
            this("ALL");
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(0).append("dataset$areacode");
            if(usStateAbbr.length() > 0) sb.append("$" + usStateAbbr);
            return sb.toString();
        }
    }

    public AreaCodeSelectionEngine(DatasetResolverRegistry resolvers) {
        this(resolvers, DEFAULT_SELECTOR_STRATEGY);
    }

    public AreaCodeSelectionEngine(DatasetResolverRegistry resolvers, SelectorStrategy<String> strategy) {
        super(resolvers, strategy);
        this.usStateMap = USState.defaultMap();
        this.usStateSelector = new WeightedSelectorImpl<>(usStateMap);
    }

    @Override
    protected void setup() {
        AreaCodeDatasetResult result = resolvers.areaCode().resolve(AreaCodeDatasetKey.defaults());
        this.datasetsByNameKey = HashMap.newHashMap(result.datasets().size());
        for (Map.Entry<String, String[]> entry : result.areaCodesByState().entrySet()) {
            RandomSelector<String> selector = new UniformSelectorImpl<>(List.of(entry.getValue()));
            datasetsByNameKey.put(new NameKey(entry.getKey()), selector);
        }
    }

    @Override
    public String select(AreaCodeDatasetKey key, SelectionFilter filter) {
        USState usState;
        if(filter.states().isPresent()) {
            // TODO: Pick a random state from set in filter
            usState = filter.states().get().stream().findFirst().get();
        } else {
            usState = usStateSelector.select();
        }

        NameKey nameKey = new NameKey(usState.name());
        RandomSelector<String> selector = datasetsByNameKey.get(nameKey);
        if (selector == null) {
            throw new IllegalStateException("No selector registered for " + nameKey);
        }

        if(filter != null && !filter.isEmpty()) {
            selector.setFilter(filter);
        }

        return selector.select();
    }

    @Override
    public AreaCodeDatasetKey defaultKey() {
        return AreaCodeDatasetKey.defaults();
    }

    @Override
    public Class<AreaCodeDatasetKey> keyType() {
        return AreaCodeDatasetKey.class;
    }

    @Override
    public Class<AreaCodeDatasetResult> resultType() {
        return AreaCodeDatasetResult.class;
    }
    
    public TemplateField field() {
        return TemplateField.AREA_CODE;
    }
}
