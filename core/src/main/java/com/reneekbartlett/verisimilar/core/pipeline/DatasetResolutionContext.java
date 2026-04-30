package com.reneekbartlett.verisimilar.core.pipeline;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

    private final Optional<Set<Generation>> generations;
    private final Optional<Set<Decade>> decades;

    private final Optional<Set<GenderIdentity>> genders;
    private final Optional<Set<Ethnicity>> ethnicities;
    private final Optional<Set<USRegion>> regions;
    private final Optional<Set<USState>> states;

    private final Optional<Set<DomainType>> domainTypes;
    private final Optional<Set<UsernameType>> usernameTypes;
    private final Optional<Set<KeywordType>> keywordTypes;

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

    public Optional<Set<Generation>> generations() { return generations; }
    public Optional<Set<Decade>> decades() { return decades; }

    public Optional<Set<GenderIdentity>> genders() { return genders; }
    public Optional<Set<Ethnicity>> ethnicities() { return ethnicities; }
    public Optional<Set<USRegion>> regions() { return regions; }
    public Optional<Set<USState>> states() { return states; }

    public Optional<Set<DomainType>> domainTypes() { return domainTypes; }
    public Optional<Set<UsernameType>> usernameTypes() { return usernameTypes; }
    public Optional<Set<KeywordType>> keywordTypes() { return keywordTypes; }

    // TODO:  For setting custom data sets
    //private final Map<TemplateField, Map<String, Double>> values;
    //public Map<TemplateField, Map<String, Double>> getResolvedValues() {
    //    return values;
    //}

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private final Map<TemplateField, Map<String, Double>> fieldValues = new HashMap<>();

        private Set<Decade> decades = new HashSet<>();
        private Set<Generation> generations = new HashSet<>();
        private Set<GenderIdentity> genders = new HashSet<>();
        private Set<Ethnicity> ethnicities = new HashSet<>();
        private Set<USRegion> regions = new HashSet<>();
        private Set<USState> states = new HashSet<>();

        private Set<DomainType> domainTypes = new HashSet<>();
        private Set<UsernameType> usernameTypes = new HashSet<>();
        private Set<KeywordType> keywordTypes = new HashSet<>();

        private Builder() {}

        public Builder gender(GenderIdentity gender) { this.genders.add(gender); return this; }
        public Builder genders(Set<GenderIdentity> genders) { this.genders.addAll(genders); return this; }

        public Builder decade(Decade decade) { this.decades.add(decade); return this; }
        public Builder decades(Set<Decade> decade) { this.decades.addAll(decade); return this; }

        public Builder generation(Generation generation) { this.generations.add(generation); return this; }
        public Builder generations(Set<Generation> generations) { this.generations.addAll(generations); return this; }

        public Builder ethnicities(Set<Ethnicity> ethnicities ) { this.ethnicities.addAll(ethnicities); return this; }
        public Builder region(USRegion region) { this.regions.add(region); return this; }
        public Builder regions(Set<USRegion> regions) { this.regions.addAll(regions); return this; }

        public Builder state(USState state) { this.states.add(state); return this; }
        public Builder states(Set<USState> states) { this.states.addAll(states); return this; }

        public Builder domainTypes(Set<DomainType> domainTypes) { this.domainTypes.addAll(domainTypes); return this; }
        public Builder usernameTypes(Set<UsernameType> usernameTypes) { this.usernameTypes.addAll(usernameTypes); return this; }
        public Builder keywordTypes(Set<KeywordType> keywordTypes) { this.keywordTypes.addAll(keywordTypes); return this; }

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
