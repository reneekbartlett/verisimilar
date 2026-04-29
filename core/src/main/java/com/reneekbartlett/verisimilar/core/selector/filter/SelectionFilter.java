package com.reneekbartlett.verisimilar.core.selector.filter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.FullName;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.PersonRecord;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.USRegion;
import com.reneekbartlett.verisimilar.core.model.USState;

public record SelectionFilter(
        Optional<String> firstName,
        Optional<String> middleName,
        Optional<String> lastName,

        Optional<GenderIdentity> gender,
        Optional<GenderIdentity[]> genders,

        Optional<LocalDate> birthday,
        Optional<Integer> minYear,
        Optional<Integer> maxYear,

        Optional<Set<USState>> states,
        Optional<Set<String>> zipCodes,
        Optional<USRegion> region,
        Optional<Ethnicity> ethnicity,

        Optional<DomainType> domainType,
        Optional<String> domain,

        Optional<String> startsWith,
        Optional<String> endsWith,
        Optional<String> contains,
        Optional<Integer> minLength,
        Optional<Integer> maxLength,

        Optional<SelectionPredicate<String>> customPredicate,
        Optional<Set<SelectionPredicate<String>>> customPredicates,

        Map<TemplateField, String> startsWithMap,
        Map<TemplateField, String> equalToMap
) {

    public SelectionFilter {
        firstName = firstName == null? Optional.empty() : firstName;
        middleName = middleName == null? Optional.empty() : middleName;
        lastName = lastName == null? Optional.empty() : lastName;

        gender = gender == null ? Optional.empty() : gender;
        genders = genders == null ? Optional.empty() : genders;

        birthday = (birthday == null) ? Optional.empty() : birthday;
        minYear = (minYear == null) ? Optional.empty() : minYear;
        maxYear = (maxYear == null) ? Optional.empty() : maxYear;

        states = states == null ? Optional.empty() : states;
        zipCodes = zipCodes == null ? Optional.empty() : zipCodes;
        region = region == null ? Optional.empty() : region;
        ethnicity = ethnicity == null ? Optional.empty() : ethnicity;

        domainType = domainType == null ? Optional.empty() : domainType;
        domain = domain == null ? Optional.empty() : domain;

        //startsWith = startsWith == null ? Optional.empty() : startsWith;
        //endsWith = endsWith == null ? Optional.empty() : endsWith;
        //contains = contains == null ? Optional.empty() : contains;
        //minLength = minLength == null ? Optional.empty() : minLength;
        //maxLength = maxLength == null ? Optional.empty() : maxLength;

        customPredicate = customPredicate == null ? Optional.empty() : customPredicate;
        customPredicates = customPredicates == null ? Optional.empty() : customPredicates;

    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public PersonRecord getPersonRecord() {
        FullName fullName = new FullName(firstName.get(), middleName.get(), lastName.get());
        return new PersonRecord(fullName, birthday.orElse(null));
    }

    public Map<String, Object> getResolvedValues() {
        Map<String, Object> values = new HashMap<>();
        if(!firstName.isEmpty()) values.put("FIRST", firstName.get());
        if(!middleName.isEmpty()) values.put("MIDDLE", middleName.get());
        if(!lastName.isEmpty()) values.put("LAST", lastName.get());
        if(!birthday.isEmpty()) {
            values.put("BIRTHDAY", birthday.get());
        }

        //if(!domain.isEmpty()) values.put("DOMAIN", domain.get());
        //if(!gender.isEmpty()) values.put("GENDER", gender.get());
        return values;
    }

    public boolean isEmpty() {
        return customPredicate.isEmpty() && customPredicates.isEmpty()
                && firstName.isEmpty() && middleName.isEmpty() && lastName.isEmpty()
                //&& gender.isEmpty()
                && birthday.isEmpty()
                && minYear.isEmpty()
                && maxYear.isEmpty()
                && states.isEmpty()
                && zipCodes.isEmpty()
                && region.isEmpty()
                && ethnicity.isEmpty()
                && domainType.isEmpty()
                && domain.isEmpty()
                && startsWith.isEmpty()
                && endsWith.isEmpty()
                && contains.isEmpty()
                && minLength.isEmpty()
                && maxLength.isEmpty()
                && startsWithMap.isEmpty()
                && equalToMap.isEmpty();
    }

    public static SelectionFilter empty() {
        return new SelectionFilter(
                // FullName
                Optional.empty(), Optional.empty(), Optional.empty(),

                // Gender / Genders
                Optional.empty(), Optional.empty(),

                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                HashMap.newHashMap(0),
                HashMap.newHashMap(0)
        );
    }

    // ------------------------------------------------------------
    // Builder
    // ------------------------------------------------------------
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String firstName;
        private String middleName;
        private String lastName;

        private GenderIdentity gender;
        private GenderIdentity[] genders;

        private LocalDate birthday;
        private Integer minYear;
        private Integer maxYear;

        private Set<USState> states;
        private Set<String> zipCodes;
        private USRegion region;
        private Ethnicity ethnicity;
        private DomainType domainType;
        private String domain;

        private String startsWith;
        private String endsWith;
        private String contains;
        private Integer minLength;
        private Integer maxLength;

        private SelectionPredicate<String> customPredicate;
        private Set<SelectionPredicate<String>> customPredicates;

        private Map<TemplateField, String> startsWithMap = HashMap.newHashMap(0);

        private Map<TemplateField, String> equalToMap = HashMap.newHashMap(0);

        public Builder() {
            //
        }

        /***
         * Copy over some existing elements.
         */
        public Builder(SelectionFilter filter) {
            this.startsWith = filter.startsWith.orElse(null);
            this.endsWith = filter.endsWith.orElse(null);
            this.contains = filter.contains.orElse(null);
            this.startsWithMap = filter.startsWithMap;
            this.equalToMap = filter.equalToMap;
        }

        public Builder firstName(String value) {
            this.firstName = value;
            return this;
        }

        public Builder middleName(String value) {
            this.middleName = value;
            return this;
        }

        public Builder lastName(String value) {
            this.lastName = value;
            return this;
        }

        public Builder gender(GenderIdentity gender) {
            this.gender = gender;
            return this;
        }

        public Builder genders(GenderIdentity... genders) {
            this.genders = genders;
            return this;
        }

        public Builder birthday(LocalDate value) {
            this.birthday = value;
            return this;
        }

        public Builder minYear(Integer value) {
            this.minYear = value;
            return this;
        }

        public Builder maxYear(Integer value) {
            this.maxYear = value;
            return this;
        }

        public Builder states(Set<USState> values) {
            this.states = values;
            Set<String> stateNames = new HashSet<>();
            for(USState state : values) {
                stateNames.add("$"+state.name()+"$");
            }
            SelectionPredicate<String> p = (val) -> stateNames.stream().anyMatch(val::contains);
            if(customPredicates == null) {
                this.customPredicate = p;
                this.customPredicates = new HashSet<>();
            }
            this.customPredicates.add(p);
            return this;
        }

        public Builder zipCodes(Set<String> values) {
            this.zipCodes = values;
            // TODO:  Also filter by state?
            Set<String> zipCodeValues = new HashSet<>();
            for(String zipCode : values) {
                zipCodeValues.add("$"+zipCode);
            }
            SelectionPredicate<String> p = (val) -> zipCodeValues.stream().anyMatch(val::contains);
            if(this.customPredicates == null) {
                this.customPredicates = new HashSet<>();
            }
            this.customPredicates.add(p);
            return this;
        }

        public Builder region(USRegion value) {
            this.region = value;
            return this;
        }

        public Builder ethnicity(Ethnicity value) {
            this.ethnicity = value;
            return this;
        }

        public Builder domainType(DomainType value) {
            this.domainType = value;
            return this;
        }
        
        public Builder domain(String value) {
            this.domain = value;
            return this;
        }

        public Builder startsWith(String value) {
            this.startsWith = value;
            return this;
        }

        public Builder startsWith(TemplateField field, String value) {
            this.startsWithMap.put(field, value);
            return this;
        }

        public Builder equalTo(String value, TemplateField field) {
            this.equalToMap.put(field, value);
            return this;
        }

        public Builder endsWith(String value) {
            this.endsWith = value;
            return this;
        }

        public Builder contains(String value) {
            // TODO: Use map
            //return this.customPredicate(key -> value.stream().anyMatch(key::contains));
            this.contains = value;
            return this;
        }

        public Builder minLength(Integer value) {
            this.minLength = value;
            return this;
        }

        public Builder maxLength(Integer value) {
            this.maxLength = value;
            return this;
        }

        public Builder customPredicate(SelectionPredicate<String> predicate) {
            this.customPredicate = predicate;
            return this;
        }

        public SelectionFilter build() {
            return new SelectionFilter(
                    Optional.ofNullable(firstName),
                    Optional.ofNullable(middleName),
                    Optional.ofNullable(lastName),

                    Optional.ofNullable(gender),
                    Optional.ofNullable(genders),
                    Optional.ofNullable(birthday),

                    Optional.ofNullable(minYear),
                    Optional.ofNullable(maxYear),

                    Optional.ofNullable(states),
                    Optional.ofNullable(zipCodes),
                    Optional.ofNullable(region),
                    Optional.ofNullable(ethnicity),
                    Optional.ofNullable(domainType),
                    Optional.ofNullable(domain),

                    Optional.ofNullable(startsWith),
                    Optional.ofNullable(endsWith),
                    Optional.ofNullable(contains),
                    Optional.ofNullable(minLength),
                    Optional.ofNullable(maxLength),

                    Optional.ofNullable(customPredicate),
                    Optional.ofNullable(customPredicates),
                    startsWithMap,
                    equalToMap
            );
        };
    }

    @Override
    public String toString() {
        final String FIELD_DELIM = " ";
        StringBuilder sb = new StringBuilder(0);
        if(!customPredicate.isEmpty()) {
            sb.append("customPredicate=" + "TRUE" + FIELD_DELIM);
        }
        if(!customPredicates.isEmpty()) {
            sb.append("customPredicates=" + "TRUE" + FIELD_DELIM);
        }
        if(!startsWith.isEmpty()) {
            sb.append("startsWith=" + this.startsWith().get() + FIELD_DELIM);
        }
        if(!startsWithMap.isEmpty()) {
            sb.append("startsWithMap.size()=" + this.startsWithMap.size() + FIELD_DELIM);
        }
        if(!endsWith.isEmpty()) {
            sb.append("endsWith=" + this.endsWith().get() + FIELD_DELIM);
        }
        if(!contains.isEmpty()) {
            sb.append("contains=" + this.contains().get() + FIELD_DELIM);
        }
        if(!firstName.isEmpty()) sb.append("firstName=" + this.firstName.get()+ FIELD_DELIM);
        if(!middleName.isEmpty()) sb.append("middleName=" + this.middleName.get()+ FIELD_DELIM);
        if(!lastName.isEmpty()) sb.append("lastName=" + this.lastName.get()+ FIELD_DELIM);
        if(!gender.isEmpty()) sb.append("genders=" + this.gender.get().name() + FIELD_DELIM);
        if(!genders.isEmpty()) {
            sb.append("genders=" + Arrays.toString(Stream.of(genders.get()).map(GenderIdentity::name).toArray(String[]::new)) + FIELD_DELIM);
        }
        if(!states.isEmpty()) {
            sb.append("states=" + String.join("$", USState.names(states.get())) + FIELD_DELIM);
        }
        if(!zipCodes.isEmpty()) {
            sb.append("zipCodes=" + String.join("$", zipCodes.get()) + FIELD_DELIM);
        }
        if(!birthday.isEmpty()) sb.append("birthday=" + this.birthday.get().toString() + FIELD_DELIM);
        if(!ethnicity.isEmpty()) sb.append("ethnicity=" + this.ethnicity.get().toString() + FIELD_DELIM);
        if(!domainType.isEmpty()) sb.append("domainType=" + this.domainType.get().toString() + FIELD_DELIM);
        if(!domain.isEmpty()) sb.append("domain=" + this.domain.get().toString() + FIELD_DELIM);
        return sb.toString();
    }
}
