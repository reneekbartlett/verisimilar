package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.HashMap;
import java.util.Map;
import com.reneekbartlett.verisimilar.core.datasets.key.FirstNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.datasets.result.FirstNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

/***
 * FirstNameDatasetKey(Gender gender, Integer year, State state):  Low-level key that tells engine which CSV to load into memory.
 * the meta-selector responsible for turning:
 * DatasetResolutionContext (formerly ConstraintContext)
 * SelectionFilter (formerly SelectionCriteria)
 * Weighted demographic selectors
 * Preloaded first-name datasets
 * …into a final weighted selector that produces a single first name.
 */
public class FirstNameSelectionEngine extends AbstractSelectionEngine<FirstNameDatasetKey,FirstNameDatasetResult> {

    //  For meta-selection (which selector to use at runtime)
    public record NameKey(GenderIdentity gender, Ethnicity ethnicity) {
        public NameKey(GenderIdentity gender) {
            this(gender, Ethnicity.UNKNOWN);
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(0).append("dataset$firstname");
            if(gender != null) sb.append("$gender:"+gender.getPlaceholder());
            if(ethnicity != null) sb.append("$ethnicity:" + ethnicity.getPlaceholder());
            return sb.toString();
        }
    }

    private static final SelectorStrategy<String> DEFAULT_SELECTOR_STRATEGY = new WeightedSelectorStrategy<>();

    private Map<GenderIdentity, Double> genderIdentityMap;
    private RandomSelector<GenderIdentity> genderSelector;

    private Map<Ethnicity, Double> ethnicitiesMap;

    private Map<NameKey, RandomSelector<String>> selectorsByNameKey;

    public FirstNameSelectionEngine(DatasetResolverRegistry resolvers) {
        this(resolvers, DEFAULT_SELECTOR_STRATEGY);
    }

    public FirstNameSelectionEngine(
            DatasetResolverRegistry resolvers, 
            SelectorStrategy<String> strategy
    ) {
        super(resolvers, strategy);
    }

    @Override
    protected void setup() {
        this.genderIdentityMap = GenderIdentity.defaultMap();
        this.genderSelector = new WeightedSelectorImpl<>(genderIdentityMap);
        this.ethnicitiesMap = Ethnicity.defaultMap();

        // Extract specific datasets (Map<String,Double>), build selectors, and add to selectorsByNameKey
        FirstNameDatasetResult firstNameDatasetResult = resolvers.first().resolve(FirstNameDatasetKey.defaults());
        this.selectorsByNameKey = HashMap.newHashMap(firstNameDatasetResult.datasets().size());
        if(ethnicitiesMap.size() >= 1){
            for(Ethnicity ethnicity : ethnicitiesMap.keySet()){
                genderIdentityMap.keySet().forEach(gender -> {
                    NameKey nameKey = new NameKey(gender, ethnicity);
                    RandomSelector<String> selector = strategy.buildSelector(firstNameDatasetResult.get(nameKey));
                    selectorsByNameKey.put(nameKey, selector);
                });
            }
        }
        //LOGGER.debug("FirstNameSelectioEngine setup complete");
        return;
    }

    @Override
    public String select(FirstNameDatasetKey key, SelectionFilter filter) {
        // If GenderIdentity is not specified, pick a random one.
        GenderIdentity gender = filter.gender().orElse(genderSelector.select());

        Ethnicity ethnicity = filter.ethnicity().orElse(Ethnicity.UNKNOWN);
        if(ethnicity != null && !ethnicitiesMap.containsKey(ethnicity)) {
            ethnicity = Ethnicity.UNKNOWN;
        }
 
        NameKey nameKey = new NameKey(gender, ethnicity);
        RandomSelector<String> selector = selectorsByNameKey.get(nameKey);

        if (selector == null) {
            throw new IllegalStateException("No selector registered for " + nameKey);
        }

        if(filter != null && !filter.isEmpty()) {
            if(!filter.firstName().isEmpty()) {
                return filter.firstName().get();
            }

            selector.setFilter(filter);
        }

        LOGGER.debug("select firstName - nameKey={}; strategyType={};", nameKey, strategy.getType());
        return selector.select();
    }

//    @Override
//    protected Map<String, Double> applyFilter(
//            Map<String, Double> map,
//            SelectionFilter filter
//    ) {
//        
//        // TODO:  Not sure this is needed...
//        // If criteria overrides gender → use that map instead
//        if (filter.genders().isPresent()) {
//            GenderIdentity[] genders = filter.genders().get();
//            var result = resolvers.first().resolve(defaultKey());
//
//            // If multiple genders are provided, pick one randomly
//            GenderIdentity g;
//            if(genders.length > 1) {
//                g = genderSelector.select();
//            } else {
//                g = genders[0];
//            }
//            map = g.isFemale() ? result.get(GenderIdentity.FEMALE) : result.get(GenderIdentity.MALE);
//            LOGGER.debug("FirstNameSelectionEngine.applyFilter=true;  filter:{} ", filter);
//        } else {
//            LOGGER.debug("FirstNameSelectionEngine.applyFilter=FALSE; filter:{} ", filter);
//        }
//
//        return super.applyFilter(map, filter);
//    }

    @Override
    public FirstNameDatasetKey defaultKey() {
        return FirstNameDatasetKey.defaults();
    }

    public Map<NameKey, RandomSelector<String>> nameSelectors(){
        return this.selectorsByNameKey;
    }

    @Override
    public Class<FirstNameDatasetKey> keyType() {
        return FirstNameDatasetKey.class;
    }

    @Override
    public Class<FirstNameDatasetResult> resultType() {
        return FirstNameDatasetResult.class;
    }

    @Override
    protected TemplateField field() {
        return TemplateField.FIRST_NAME;
    }
}
