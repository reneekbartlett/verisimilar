package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.NicknameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.datasets.result.NicknameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorStrategy;

public class NicknameSelectionEngine extends AbstractSelectionEngine<NicknameDatasetKey, NicknameDatasetResult> {
    private static final SelectorStrategy<String> DEFAULT_SELECTOR_STRATEGY = new WeightedSelectorStrategy<>();
    private Map<NameKey, RandomSelector<String>> selectorsByNameKey;

    public record NameKey(GenderIdentity gender, Ethnicity ethnicity) {
        public NameKey(GenderIdentity gender) {
            this(gender, Ethnicity.UNKNOWN);
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(0).append("dataset$nickname");
            if(gender != null) sb.append("$gender:"+gender.name());
            if(ethnicity != null) sb.append("$ethnicity:"+ethnicity.name());
            return sb.toString();
        }
    }

    public NicknameSelectionEngine(DatasetResolverRegistry resolvers) {
        this(resolvers, DEFAULT_SELECTOR_STRATEGY);
    }

    public NicknameSelectionEngine(DatasetResolverRegistry resolvers, SelectorStrategy<String> strategy) {
        super(resolvers, strategy);
    }

    protected void setup() {
        NicknameDatasetResult result = resolvers.nickname().resolve(NicknameDatasetKey.defaults());
        result.datasets().forEach((nameKey, map) -> {
            RandomSelector<String> selector = strategy.buildSelector(map);
            selectorsByNameKey.put(nameKey, selector);
        });
        //LOGGER.debug("setup - result:{}", result.toString());
    }

    @Override
    public NicknameDatasetKey defaultKey() {
        return NicknameDatasetKey.defaults();
    }

    @Override
    public Class<NicknameDatasetKey> keyType() {
        return NicknameDatasetKey.class;
    }

    @Override
    public Class<NicknameDatasetResult> resultType() {
        return NicknameDatasetResult.class;
    }
}