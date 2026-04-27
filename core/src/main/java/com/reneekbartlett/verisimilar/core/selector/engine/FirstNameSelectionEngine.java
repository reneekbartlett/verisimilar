package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.datasets.key.FirstNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.datasets.result.FirstNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
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
            this(gender, null);
        }
        @Override
        public String toString() {
            return new StringBuilder(0).append("dataset$firstname")
                    .append("$"+gender.name())
                    .append("$"+ethnicity.name())
                    .toString();
        }
    }

    private static final SelectorStrategy<String> DEFAULT_SELECTOR_STRATEGY = new WeightedSelectorStrategy<>();

    private Map<GenderIdentity, Double> genderIdentityMap;
    private RandomSelector<GenderIdentity> genderSelector;

    private Map<Ethnicity, Double> ethnicitiesMap;
    private RandomSelector<Ethnicity> ethnicitySelector;

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

        // TODO:  Create cfg_fullname_first_female_ETHNICITY.csv & cfg_fullname_first_male_ETHNICITY.csv files.
        //this.ethnicitiesMap = Ethnicity.defaultMap();
        this.ethnicitiesMap = Map.of(Ethnicity.UNKNOWN, 0.0001);
        this.ethnicitySelector = new WeightedSelectorImpl<>(ethnicitiesMap);

        // This DatasetResult contains both genders, so only call once.
        FirstNameDatasetResult firstNameDatasetResult = resolvers.first().resolve(FirstNameDatasetKey.defaults());

        this.selectorsByNameKey = HashMap.newHashMap(firstNameDatasetResult.datasets().size());

        RandomSelector<String> firstNameFemaleSelector = strategy.buildSelector(firstNameDatasetResult.get(GenderIdentity.FEMALE));
        selectorsByNameKey.put(new NameKey(GenderIdentity.FEMALE), firstNameFemaleSelector);

        RandomSelector<String> firstNameMaleSelector = strategy.buildSelector(firstNameDatasetResult.get(GenderIdentity.MALE));
        selectorsByNameKey.put(new NameKey(GenderIdentity.MALE), firstNameMaleSelector);

        // TODO: Revise logic?  Move to FirstNameDatasetResult?
        // Extract specific datasets (Map<String,Double>) and build selectors
        //    Then Build routing map (Gender + Ethnicity → WeightedSelector<String>)
        Set<Ethnicity> customEthnicities = ethnicitiesMap.keySet().stream()
                .filter(s -> !s.getLabel().isEmpty() && s != Ethnicity.UNKNOWN) // for firstnames, don't include UNKNOWN
                .collect(Collectors.toSet());
        if(customEthnicities.size() >= 1){
            for(Ethnicity ethnicity : customEthnicities){
                genderIdentityMap.keySet().forEach(gender -> {
                    FirstNameDatasetResult ds1 = resolvers.first().resolve(new FirstNameDatasetKey(gender, ethnicity));
                    RandomSelector<String> s1 = strategy.buildSelector(ds1.get(gender, ethnicity));
                    selectorsByNameKey.put(new NameKey(gender, ethnicity), s1);
                });
            }
        }
    }

    @Override
    public String select(FirstNameDatasetKey key, SelectionFilter filter) {
        // If GenderIdentity is not specified, pick a random one.
        GenderIdentity gender = filter.gender().orElse(genderSelector.select());

        // TODO: If Ethnicity is NOT supplied here, just select from dataset based on GenderIdentity.
        Ethnicity ethnicity = filter.ethnicity().orElse(null);

        NameKey nameKey = new NameKey(gender, ethnicity);
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
    protected Map<String, Double> applyFilter(
            Map<String, Double> map,
            SelectionFilter filter
    ) {
        
        // TODO:  Not sure this is needed...
        // If criteria overrides gender → use that map instead
        if (filter.genders().isPresent()) {
            GenderIdentity[] genders = filter.genders().get();
            var result = resolvers.first().resolve(defaultKey());

            // If multiple genders are provided, pick one randomly
            GenderIdentity g;
            if(genders.length > 1) {
                g = genderSelector.select();
            } else {
                g = genders[0];
            }
            map = g.isFemale() ? result.get(GenderIdentity.FEMALE) : result.get(GenderIdentity.MALE);
            LOGGER.debug("applyFilter: " + filter.toString());
        }

        return super.applyFilter(map, filter);
    }

    @Override
    public FirstNameDatasetKey defaultKey() {
        return FirstNameDatasetKey.defaults();
    }

    public Map<NameKey, RandomSelector<String>> nameSelectors(){
        return this.selectorsByNameKey;
    }

    public RandomSelector<Ethnicity> ethnicitySelector(){
        return this.ethnicitySelector;
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
