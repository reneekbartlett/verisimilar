package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.reneekbartlett.verisimilar.core.datasets.key.AreaCodeDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.resolver.AreaCodeDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.datasets.result.AreaCodeDatasetResult;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.UniformSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.UniformSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

/***
 * Map<String, String[]> AREACODES_BY_STATE = ResourceMapLoader.loadArrayMap("/cfg_phone_number_area_code_bystate_ALL.csv");
 * AZ,480|520|602|623|928
 */
public class AreaCodeSelectionEngine extends AbstractSelectionEngine<AreaCodeDatasetKey, AreaCodeDatasetResult> {
    private static final SelectorStrategy<String> DEFAULT_SELECTOR_STRATEGY = new UniformSelectorStrategy<>();
    private Set<USState> usStateOptions; // EnumSet
    private Map<NameKey, RandomSelector<String>> selectorsByNameKey;

    //  For meta-selection (which selector to use at runtime)
    public record NameKey(String usStateAbbr) {
        public NameKey() {
            this("ALL");
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(0).append("dataset$area_code");
            if(usStateAbbr.length() > 0) sb.append("$us_state:" + usStateAbbr);
            return sb.toString();
        }
    }

    public AreaCodeSelectionEngine(DatasetResolverRegistry resolvers) {
        this(resolvers, DEFAULT_SELECTOR_STRATEGY);
    }

    public AreaCodeSelectionEngine(DatasetResolverRegistry resolvers, SelectorStrategy<String> strategy) {
        super(resolvers.areaCode(), strategy);
    }

    public AreaCodeSelectionEngine(AreaCodeDatasetResolver resolver) {
        super(resolver, DEFAULT_SELECTOR_STRATEGY);
    }

    @Override
    protected void setup() {
        AreaCodeDatasetResult result = datasetResolver().resolve(AreaCodeDatasetKey.defaults());
        this.selectorsByNameKey = HashMap.newHashMap(result.datasets().size());
        result.datasets().forEach((nameKey, map) -> {
            RandomSelector<String> selector = strategy.buildSelector(map, field());
            selectorsByNameKey.put(nameKey, selector);
        });

        this.usStateOptions = USState.defaultDatasets();
        //LOGGER.debug("AreaCodeDatasetResult=[{}]", result);
    }

    @Override
    public String select(AreaCodeDatasetKey key, SelectionFilter filter) {
        // TODO:  Revise/streamline logic

        USState usState;
        if(filter != null) {
            // If area code is already populated, just return it.
            if(filter.areaCode().isPresent()) {
                return filter.areaCode().get();
            }
            if(filter.equalToMap().containsKey(field())) {
                return filter.equalToMap().get(field());
            }

            // Get state from filter or pick a random one to speed up selection.
            if(!filter.state().isPresent()) {
                filter.states().ifPresent(values -> this.setStateOptions(values));
                usState = getRandomState();
            } else {
                usState = filter.state().get();
            }
        } else {
            usState = getRandomState();
        }

        NameKey nameKey = new NameKey(usState.name());
        RandomSelector<String> selector = selectorsByNameKey.get(nameKey);
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

    @Override
    public TemplateField field() {
        return TemplateField.AREA_CODE;
    }

    private void setStateOptions(Set<USState> usStateOptions) {
        this.usStateOptions = usStateOptions;
    }

    /***
     * 
     * @return
     */
    private USState getRandomState() {
        RandomSelector<USState> stateSelector = new UniformSelectorImpl<>(usStateOptions, TemplateField.STATE);
        return stateSelector.select();
    }
}
