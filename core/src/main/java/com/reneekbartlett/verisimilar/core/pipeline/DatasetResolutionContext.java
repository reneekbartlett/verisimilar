package com.reneekbartlett.verisimilar.core.pipeline;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.USRegion;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.model.UsernameType;

/***
 * DatasetResolutionContext is for dataset resolution.  
 * Purpose: Choose the dataset before filtering.
 * This class carries global, structural, upstream parameters that determine which dataset should be loaded or selected.
 */
public final class DatasetResolutionContext {

    private final Map<String, Object> values;
    public static Builder builder() { return new Builder(); }

    /**
     * Returns all resolved values used by generators, selectors, and templates.
     */
    public Map<String, Object> getResolvedValues() {
        return values;
    }

    // Common dimensions
    private final Optional<Integer> year;
    //private final Integer[] years;

    // First name–specific
    private final Set<GenderIdentity> genders;

    // Last name–specific
    private final Optional<Ethnicity> ethnicity;
    private final Set<Ethnicity> ethnicities;

    // Street name–specific
    private final Set<USRegion> regions;
    private final Optional<USRegion> region;

    private final Set<USState> states;
    private final Set<String> zipCodes;

    private final Set<DomainType> domainTypes;
    private final Set<UsernameType> usernameTypes;

    private DatasetResolutionContext(Builder b) {
        this.year = Optional.ofNullable(b.year);
        //this.gender = b.gender;
        this.genders = b.genders;

        this.ethnicity = Optional.ofNullable(b.ethnicity);
        this.ethnicities = b.ethnicities;

        this.region = Optional.ofNullable(b.region);
        this.regions = b.regions;
        this.states = b.states;
        this.zipCodes = b.zipCodes;
        this.domainTypes = b.domainTypes;
        this.usernameTypes = b.usernameTypes;

        this.values = Collections.unmodifiableMap(b.values);
    }

    public Optional<Integer> year() { return year; }
    //public Integer[] years() { return years; }
    //public GenderIdentity gender() { return gender; }
    public Set<GenderIdentity> genders() { return genders; }
    public Set<USState> states() { return states; }
    public Set<String> zipCodes() { return zipCodes; }

    public Optional<Ethnicity> ethnicity() { return ethnicity; }
    public Set<Ethnicity> ethnicities() { return ethnicities; }
    
    public Optional<USRegion> region() { return region; }
    public Set<USRegion> regions() { return regions; }

    public Set<DomainType> domainTypes() { return domainTypes; }
    public Set<UsernameType> usernameTypes() { return usernameTypes; }

    public static final class Builder {
        private final Map<String, Object> values = new HashMap<>();
        public Builder put(String key, Object value) {
            if (value != null) {
                values.put(key, value);
            }
            return this;
        }

        public Builder putAll(Map<String, Object> map) {
            map.forEach(this::put);
            return this;
        }

        private Integer year;
        //private Integer[] years;

        private GenderIdentity gender;
        private Set<GenderIdentity> genders;

        private USRegion region;
        private Set<USRegion> regions;
        private Set<USState> states;
        private Set<String> zipCodes;

        private Ethnicity ethnicity;
        private Set<Ethnicity> ethnicities;

        private Set<DomainType> domainTypes;
        private Set<UsernameType> usernameTypes;

        private Builder() {}

        public Builder gender(GenderIdentity gender) { this.gender = gender; return this; }
        public Builder genders(Set<GenderIdentity> genders) { this.genders = genders; return this; }

        public Builder ethnicities(Set<Ethnicity> ethnicities ) { this.ethnicities = ethnicities; return this; }

        public Builder region(USRegion region) { this.region = region; return this; }
        public Builder regions(Set<USRegion> regions) { this.regions = regions; return this; }
        public Builder states(Set<USState> states) { this.states = states; return this; }
        public Builder zipCodes(Set<String> zipCodes) { this.zipCodes = zipCodes; return this; }

        public Builder domainTypes(Set<DomainType> domainTypes) { this.domainTypes = domainTypes; return this; }
        public Builder usernameTypes(Set<UsernameType> usernameTypes) { this.usernameTypes = usernameTypes; return this; }

        public DatasetResolutionContext build() {
            // TODO
            if(gender != null) this.put("GENDER", this.gender);
            return new DatasetResolutionContext(this);
        }
    }

    public static DatasetResolutionContext empty() {
        return new DatasetResolutionContext.Builder().build();
    }
}
