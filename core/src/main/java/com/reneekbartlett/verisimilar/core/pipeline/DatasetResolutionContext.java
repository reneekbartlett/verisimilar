package com.reneekbartlett.verisimilar.core.pipeline;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.reneekbartlett.verisimilar.core.model.Decade;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.Generation;
import com.reneekbartlett.verisimilar.core.model.KeywordType;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.USRegion;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.model.UsernameType;

/***
 * DatasetResolutionContext is for dataset resolution.  
 * Purpose: Choose the dataset before filtering.
 * This class carries global, structural, upstream parameters that determine which dataset should be loaded or selected.
 */
public final class DatasetResolutionContext {

    private final Optional<EnumSet<Generation>> generations;
    private final Optional<EnumSet<Decade>> decades;

    private final Optional<EnumSet<GenderIdentity>> genders;
    private final Optional<EnumSet<Ethnicity>> ethnicities;
    private final Optional<EnumSet<USRegion>> regions;
    private final Optional<EnumSet<USState>> states;

    private final Optional<EnumSet<DomainType>> domainTypes;
    private final Optional<EnumSet<UsernameType>> usernameTypes;
    private final Optional<EnumSet<KeywordType>> keywordTypes;

    private DatasetResolutionContext(Builder b) {
        //
        // Enums have the weight field populated.
        this.generations = Optional.ofNullable(!b.generations.isEmpty() ? b.generations : null);
        this.decades = Optional.ofNullable(!b.decades.isEmpty() ? b.decades : null);
        this.genders = Optional.ofNullable(!b.genders.isEmpty() ? b.genders : null);
        this.ethnicities = Optional.ofNullable(!b.ethnicities.isEmpty() ? b.ethnicities : null);
        this.regions = Optional.ofNullable(!b.regions.isEmpty() ? b.regions : null);
        this.states = Optional.ofNullable(!b.states.isEmpty() ? b.states : null);
        this.domainTypes = Optional.ofNullable(!b.domainTypes.isEmpty() ? b.domainTypes : null);
        this.usernameTypes = Optional.ofNullable(!b.usernameTypes.isEmpty() ? b.usernameTypes : null);
        this.keywordTypes = Optional.ofNullable(!b.keywordTypes.isEmpty() ? b.keywordTypes : null);

        // For setting custom data sets
        //this.values = Collections.unmodifiableMap(b.values);
    }

    public Optional<EnumSet<Generation>> generations() { return generations; }
    public Optional<EnumSet<Decade>> decades() { return decades; }

    public Optional<EnumSet<GenderIdentity>> genders() { return genders; }
    public Optional<EnumSet<Ethnicity>> ethnicities() { return ethnicities; }
    public Optional<EnumSet<USRegion>> regions() { return regions; }
    public Optional<EnumSet<USState>> states() { return states; }

    public Optional<EnumSet<DomainType>> domainTypes() { return domainTypes; }
    public Optional<EnumSet<UsernameType>> usernameTypes() { return usernameTypes; }
    public Optional<EnumSet<KeywordType>> keywordTypes() { return keywordTypes; }

    // TODO:  For setting custom data sets
    //private final Map<TemplateField, Map<String, Double>> values;
    //public Map<TemplateField, Map<String, Double>> getResolvedValues() {
    //    return values;
    //}

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private final Map<TemplateField, Map<String, Double>> fieldValues = new HashMap<>();

        private EnumSet<Decade> decades = EnumSet.noneOf(Decade.class);
        private EnumSet<Generation> generations = EnumSet.noneOf(Generation.class);
        private EnumSet<GenderIdentity> genders = EnumSet.noneOf(GenderIdentity.class);
        private EnumSet<Ethnicity> ethnicities = EnumSet.noneOf(Ethnicity.class);
        private EnumSet<USRegion> regions = EnumSet.noneOf(USRegion.class);
        private EnumSet<USState> states = EnumSet.noneOf(USState.class);

        private EnumSet<DomainType> domainTypes= EnumSet.noneOf(DomainType.class);
        private EnumSet<UsernameType> usernameTypes= EnumSet.noneOf(UsernameType.class);
        private EnumSet<KeywordType> keywordTypes= EnumSet.noneOf(KeywordType.class);

        private Builder() {}

        public Builder gender(GenderIdentity gender) { this.genders = EnumSet.of(gender); return this; }
        public Builder genders(EnumSet<GenderIdentity> genders) { 
            this.genders = genders;
            return this; 
        }

        public Builder decade(Decade decade) { this.decades.add(decade); return this; }
        public Builder decades(EnumSet<Decade> decade) { this.decades.addAll(decade); return this; }

        public Builder generation(Generation generation) { this.generations.add(generation); return this; }
        public Builder generations(EnumSet<Generation> generations) { this.generations.addAll(generations); return this; }

        public Builder ethnicities(EnumSet<Ethnicity> ethnicities ) { this.ethnicities.addAll(ethnicities); return this; }
        public Builder region(USRegion region) { this.regions.add(region); return this; }
        public Builder regions(EnumSet<USRegion> regions) { this.regions.addAll(regions); return this; }

        public Builder state(USState state) { this.states.add(state); return this; }
        public Builder states(EnumSet<USState> states) { this.states.addAll(states); return this; }

        public Builder domainTypes(EnumSet<DomainType> domainTypes) { this.domainTypes.addAll(domainTypes); return this; }
        public Builder usernameTypes(EnumSet<UsernameType> usernameTypes) { this.usernameTypes.addAll(usernameTypes); return this; }
        public Builder keywordTypes(EnumSet<KeywordType> keywordTypes) { this.keywordTypes.addAll(keywordTypes); return this; }

        // TODO:  For setting custom weights/values for data sets
        public Builder put(TemplateField field, Map<String, Double> values) {
            if (values != null) {
                fieldValues.put(field, values);
            }
            return this;
        }

        // TODO:  For setting custom weights/values for data sets
        public Builder putAll(Map<TemplateField, Map<String, Double>> map) {
            map.forEach(this::put);
            return this;
        }

        public DatasetResolutionContext build() {
            return new DatasetResolutionContext(this);
        }
    }

    public static DatasetResolutionContext empty() {
        return new DatasetResolutionContext.Builder().build();
    }
}
