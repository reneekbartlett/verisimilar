package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.datasets.key.MiddleNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.datasets.result.MiddleNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class MiddleNameSelectionEngine extends AbstractSelectionEngine<MiddleNameDatasetKey, MiddleNameDatasetResult> {
    private static final SelectorStrategy<String> DEFAULT_SELECTOR_STRATEGY = new WeightedSelectorStrategy<>();
    private Map<GenderIdentity, Double> genderIdentityMap;
    private RandomSelector<GenderIdentity> genderSelector;
    private Map<Ethnicity, Double> ethnicitiesMap;
    private Map<NameKey, RandomSelector<String>> selectorsByNameKey;

    public record NameKey(GenderIdentity gender, Ethnicity ethnicity) {
        public NameKey(GenderIdentity gender) {
            this(gender, null);
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(0).append("dataset$middlename");
            if(gender != null) sb.append("$"+gender.name());
            if(ethnicity != null) sb.append("$"+ethnicity.name());
            return sb.toString();
        }
    }

    public MiddleNameSelectionEngine(DatasetResolverRegistry resolvers) {
        this(resolvers, DEFAULT_SELECTOR_STRATEGY);
    }

    public MiddleNameSelectionEngine(
            DatasetResolverRegistry resolvers, 
            SelectorStrategy<String> strategy
    ) {
        super(resolvers, strategy);
    }

    protected void setup() {
        this.genderIdentityMap = GenderIdentity.defaultMap(); // TODO
        this.genderSelector = new WeightedSelectorImpl<>(genderIdentityMap);

        // TODO:  Create cfg_fullname_middle_female_ETHNICITY.csv & cfg_fullname_middle_male_ETHNICITY.csv files.
        this.ethnicitiesMap = Map.of(Ethnicity.UNKNOWN, 0.0001);

        // This DatasetResult contains both genders, so only call once.
        MiddleNameDatasetResult middleNameDatasetResult = resolvers.middle().resolve(MiddleNameDatasetKey.defaults());

        this.selectorsByNameKey = HashMap.newHashMap(middleNameDatasetResult.datasets().size());

        RandomSelector<String> middleNameFemaleSelector = strategy.buildSelector(middleNameDatasetResult.get(GenderIdentity.FEMALE));
        selectorsByNameKey.put(new NameKey(GenderIdentity.FEMALE), middleNameFemaleSelector);

        RandomSelector<String> middleNameMaleSelector = strategy.buildSelector(middleNameDatasetResult.get(GenderIdentity.MALE));
        selectorsByNameKey.put(new NameKey(GenderIdentity.MALE), middleNameMaleSelector);

        // TODO:  MiddleName Ethnicity is statically set for now.. will always be unknown.
        Set<Ethnicity> customEthnicities = ethnicitiesMap.keySet().stream()
                .filter(s -> !s.getLabel().isEmpty() && s != Ethnicity.UNKNOWN) // for first+middle names, don't include UNKNOWN
                .collect(Collectors.toSet());

        if(customEthnicities.size() >= 1){
            for(Ethnicity ethnicity : customEthnicities){
                genderIdentityMap.keySet().forEach(gender -> {
                    MiddleNameDatasetResult ds1 = resolvers.middle().resolve(new MiddleNameDatasetKey(gender));
                    RandomSelector<String> s1 = strategy.buildSelector(ds1.get(gender, ethnicity));
                    selectorsByNameKey.put(new NameKey(gender, ethnicity), s1);
                });
            }
        }
    }

    @Override
    public String select(MiddleNameDatasetKey key, SelectionFilter filter) {
        // If GenderIdentity is not specified, pick a random 1 then only return first letter.
        // TODO:  Add neutral file
        GenderIdentity gender = filter.gender().orElse(genderSelector.select());

        // TODO: If Ethnicity is NOT supplied here, just select from dataset based on GenderIdentity.
        //Ethnicity ethnicity = criteria.ethnicity().orElse(Ethnicity.UNKNOWN);
        //Ethnicity ethnicity = null;

        NameKey nameKey = new NameKey(gender, null);
        RandomSelector<String> selector = selectorsByNameKey.get(nameKey);

        if(filter != null && !filter.isEmpty()) {
            if(filter.gender().isEmpty()) {
                // Just return first letter
                return selector.select().substring(0, 1);
            }
            selector.setFilter(filter);
        }

        return selector.select();
    }

    @Override
    protected Map<String, Double> applyFilter(
            Map<String, Double> map,
            SelectionFilter filter
    ) {
        //LOGGER.debug("MiddleName.applyCriteria: " + criteria.toString());
        return super.applyFilter(map, filter);
    }

    @Override
    public MiddleNameDatasetKey defaultKey() {
        return MiddleNameDatasetKey.defaults();
    }

    @Override
    public Class<MiddleNameDatasetKey> keyType() {
        return MiddleNameDatasetKey.class;
    }

    @Override
    public Class<MiddleNameDatasetResult> resultType() {
        return MiddleNameDatasetResult.class;
    }
}
