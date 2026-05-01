//package com.reneekbartlett.verisimilar.core.selector.filter;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.EnumSet;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Optional;
//import java.util.Set;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import com.reneekbartlett.verisimilar.core.model.DomainType;
//import com.reneekbartlett.verisimilar.core.model.Ethnicity;
//import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
//import com.reneekbartlett.verisimilar.core.model.TemplateField;
//import com.reneekbartlett.verisimilar.core.model.USRegion;
//import com.reneekbartlett.verisimilar.core.model.USState;
//
//public class SelectFilter {
//
//    private final Optional<String> firstName;
//    private final Optional<String> middleName;
//    private final Optional<String> lastName;
//
//    private final Optional<GenderIdentity> gender;
//    private final Optional<EnumSet<GenderIdentity>> genders;
//
//    private final Optional<LocalDate> birthday;
//    private Optional<Integer> minYear;
//    private final Optional<Integer> maxYear;
//
//    private final Optional<EnumSet<USState>> states;
//    private final Optional<Set<String>> zipCodes;
//    private final Optional<USRegion> region;
//    private final Optional<Ethnicity> ethnicity;
//
//    private final Optional<DomainType> domainType;
//    private final Optional<String> domain;
//
//    private final Optional<String> startsWith;
//    private final Optional<String> endsWith;
//    private final Optional<String> contains;
//    private final Optional<Integer> minLength;
//    private final Optional<Integer> maxLength;
//
//    private final Optional<SelectionPredicate<String>> customPredicate;
//    private final Optional<Set<SelectionPredicate<String>>> customPredicates;
//
//    private Map<TemplateField, String> startsWithMap = HashMap.newHashMap(1);
//
//    public SelectFilter(Optional<String> firstName,
//            Optional<String> middleName,
//            Optional<String> lastName,
//
//            Optional<GenderIdentity> gender,
//            Optional<EnumSet<GenderIdentity>> genders,
//
//            Optional<LocalDate> birthday,
//            Optional<Integer> minYear,
//            Optional<Integer> maxYear,
//
//            Optional<EnumSet<USState>> states,
//            Optional<Set<String>> zipCodes,
//            Optional<USRegion> region,
//            Optional<Ethnicity> ethnicity,
//
//            Optional<DomainType> domainType,
//            Optional<String> domain,
//
//            Optional<String> startsWith,
//            Optional<String> endsWith,
//            Optional<String> contains,
//            Optional<Integer> minLength,
//            Optional<Integer> maxLength,
//
//            Optional<SelectionPredicate<String>> customPredicate,
//            Optional<Set<SelectionPredicate<String>>> customPredicates,
//
//            Map<TemplateField, String> startsWithMap
//            ) {
//        this.firstName = firstName == null? Optional.empty() : firstName;
//        this.middleName = middleName == null? Optional.empty() : middleName;
//        this.lastName = lastName == null? Optional.empty() : lastName;
//
//        this.gender = gender == null ? Optional.empty() : gender;
//        this.genders = genders == null ? Optional.empty() : genders;
//
//        this.birthday = (birthday == null) ? Optional.empty() : birthday;
//        this.minYear = (minYear == null) ? Optional.empty() : minYear;
//        this.maxYear = (maxYear == null) ? Optional.empty() : maxYear;
//
//        this.states = states == null ? Optional.empty() : states;
//        this.zipCodes = zipCodes == null ? Optional.empty() : zipCodes;
//        this.region = region == null ? Optional.empty() : region;
//        this.ethnicity = ethnicity == null ? Optional.empty() : ethnicity;
//
//        this.domainType = domainType == null ? Optional.empty() : domainType;
//        this.domain = domain == null ? Optional.empty() : domain;
//
//        this.startsWith = startsWith == null ? Optional.empty() : startsWith;
//        this.endsWith = endsWith == null ? Optional.empty() : endsWith;
//        this.contains = contains == null ? Optional.empty() : contains;
//        this.minLength = minLength == null ? Optional.empty() : minLength;
//        this.maxLength = maxLength == null ? Optional.empty() : maxLength;
//
//        this.customPredicate = customPredicate == null ? Optional.empty() : customPredicate;
//        this.customPredicates = customPredicates == null ? Optional.empty() : customPredicates;
//
//        this.startsWithMap = startsWithMap;
//    }
//
//    public Optional<Set<SelectionPredicate<String>>> customPredicates(){
//        return this.customPredicates;
//    }
//
//    public Map<TemplateField, String> startsWithMap(){
//        return this.startsWithMap;
//    }
//
//    public Builder toBuilder() {
//        return new Builder(this);
//    }
//
//    public Map<String, Object> getResolvedValues() {
//        Map<String, Object> values = new HashMap<>();
//        if(!firstName.isEmpty()) values.put("FIRST", firstName.get());
//        if(!middleName.isEmpty()) values.put("MIDDLE", middleName.get());
//        if(!lastName.isEmpty()) values.put("LAST", lastName.get());
//        if(!birthday.isEmpty()) {
//            values.put("BIRTHDAY", birthday.get());
//        }
//
//        //if(!domain.isEmpty()) values.put("DOMAIN", domain.get());
//        //if(!gender.isEmpty()) values.put("GENDER", gender.get());
//        return values;
//    }
//
//    public boolean isEmpty() {
//        return customPredicate.isEmpty() && customPredicates.isEmpty()
//                && firstName.isEmpty() && middleName.isEmpty() && lastName.isEmpty()
//                //&& gender.isEmpty()
//                && birthday.isEmpty()
//                && minYear.isEmpty()
//                && maxYear.isEmpty()
//                && states.isEmpty()
//                && zipCodes.isEmpty()
//                && region.isEmpty()
//                && ethnicity.isEmpty()
//                && domainType.isEmpty()
//                && domain.isEmpty()
//                && startsWith.isEmpty()
//                && endsWith.isEmpty()
//                && contains.isEmpty()
//                && minLength.isEmpty()
//                && maxLength.isEmpty()
//                && startsWithMap.isEmpty();
//    }
//
//    public static SelectFilter empty() {
//        return new SelectFilter(
//                // FullName
//                Optional.empty(), Optional.empty(), Optional.empty(),
//
//                // Gender / Genders
//                Optional.empty(), Optional.empty(),
//
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                null
//        );
//    }
//
//    // ------------------------------------------------------------
//    // Builder
//    // ------------------------------------------------------------
//    public static Builder builder() {
//        return new Builder();
//    }
//
//    public static final class Builder {
//        private String firstName;
//        private String middleName;
//        private String lastName;
//
//        private GenderIdentity gender;
//        private GenderIdentity[] genders;
//
//        private LocalDate birthday;
//        private Integer minYear;
//        private Integer maxYear;
//
//        private Set<USState> states;
//        private Set<String> zipCodes;
//        private USRegion region;
//        private Ethnicity ethnicity;
//        private DomainType domainType;
//        private String domain;
//
//        private String startsWith;
//        private String endsWith;
//        private String contains;
//        private Integer minLength;
//        private Integer maxLength;
//
//        private SelectionPredicate<String> customPredicate;
//        private Set<SelectionPredicate<String>> customPredicates;
//
//        private Map<TemplateField, String> startsWithMap = HashMap.newHashMap(1);
//
//        //private GenderIdentity genderOverride;
//        //private Ethnicity ethnicityOverride;
//
//        public Builder() {
//            //
//        }
//
//        public Builder(SelectFilter filter) {
//            //this.domain = filter.domain.orElse(null);
//            this.startsWith = filter.startsWith.orElse(null);
//            this.endsWith = filter.endsWith.orElse(null);
//            this.contains = filter.contains.orElse(null);
//            //this.customPredicates = filter.customPredicates.orElse(HashSet.newHashSet(0));
//            
//            
//            
//        }
//
//        public Builder firstName(String value) {
//            this.firstName = value;
//            return this;
//        }
//
//        public Builder middleName(String value) {
//            this.middleName = value;
//            return this;
//        }
//
//        public Builder lastName(String value) {
//            this.lastName = value;
//            return this;
//        }
//
//        public Builder gender(GenderIdentity gender) {
//            this.gender = gender;
//            return this;
//        }
//
//        public Builder genders(GenderIdentity... genders) {
//            this.genders = genders;
//            return this;
//        }
//
//        public Builder birthday(LocalDate value) {
//            this.birthday = value;
//            return this;
//        }
//
//        public Builder minYear(Integer value) {
//            this.minYear = value;
//            return this;
//        }
//
//        public Builder maxYear(Integer value) {
//            this.maxYear = value;
//            return this;
//        }
//
//        public Builder states(Set<USState> values) {
//            this.states = values;
//            Set<String> stateNames = new HashSet<>();
//            for(USState state : values) {
//                stateNames.add("$"+state.name()+"$");
//            }
//            SelectionPredicate<String> p = (val) -> stateNames.stream().anyMatch(val::contains);
//            if(customPredicates == null) {
//                this.customPredicate = p;
//                this.customPredicates = new HashSet<>();
//            }
//            this.customPredicates.add(p);
//            return this;
//        }
//
//        public Builder zipCodes(Set<String> values) {
//            this.zipCodes = values;
//            // TODO:  Also filter by state?
//            Set<String> zipCodeValues = new HashSet<>();
//            for(String zipCode : values) {
//                zipCodeValues.add("$"+zipCode);
//            }
//            SelectionPredicate<String> p = (val) -> zipCodeValues.stream().anyMatch(val::contains);
//            if(this.customPredicates == null) {
//                this.customPredicates = new HashSet<>();
//            }
//            this.customPredicates.add(p);
//            return this;
//        }
//
//        public Builder region(USRegion value) {
//            this.region = value;
//            return this;
//        }
//
//        public Builder ethnicity(Ethnicity value) {
//            this.ethnicity = value;
//            return this;
//        }
//
//        public Builder domainType(DomainType value) {
//            this.domainType = value;
//            return this;
//        }
//        
//        public Builder domain(String value) {
//            this.domain = value;
//            return this;
//        }
//
//        public Builder startsWith(String value) {
//            this.startsWith = value;
//            return this;
//        }
//
//        public Builder startsWith(TemplateField field, String value) {
//            //this.startsWith = value;
//            //SelectionPredicate<String> p = (val) -> stateNames.stream().anyMatch(val::contains);
//            this.startsWithMap.put(field, value);
//            return this;
//        }
//
//        public Builder endsWith(String value) {
//            this.endsWith = value;
//            return this;
//        }
//
//        public Builder contains(String value) {
//            // TODO
//            //return this.customPredicate(key -> value.stream().anyMatch(key::contains));
//            this.contains = value;
//            return this;
//        }
//
//        public Builder minLength(Integer value) {
//            this.minLength = value;
//            return this;
//        }
//
//        public Builder maxLength(Integer value) {
//            this.maxLength = value;
//            return this;
//        }
//
//        public Builder customPredicate(SelectionPredicate<String> predicate) {
//            this.customPredicate = predicate;
//            return this;
//        }
//
//        //public Builder genderOverride(GenderIdentity gender) {
//        //    this.genderOverride = gender;
//        ////    return this;
//        //}
//
//        //public Builder ethnicityOverride(Ethnicity ethnicity) {
//        //    this.ethnicityOverride = ethnicity;
//        //    return this;
//        //}
//
//        public SelectFilter build() {
//            return new SelectFilter(
//                    Optional.ofNullable(firstName),
//                    Optional.ofNullable(middleName),
//                    Optional.ofNullable(lastName),
//
//                    Optional.ofNullable(gender),
//                    Optional.ofNullable(genders),
//                    Optional.ofNullable(birthday),
//
//                    Optional.ofNullable(minYear),
//                    Optional.ofNullable(maxYear),
//
//                    Optional.ofNullable(states),
//                    Optional.ofNullable(zipCodes),
//                    Optional.ofNullable(region),
//                    Optional.ofNullable(ethnicity),
//                    Optional.ofNullable(domainType),
//                    Optional.ofNullable(domain),
//
//                    Optional.ofNullable(startsWith),
//                    Optional.ofNullable(endsWith),
//                    Optional.ofNullable(contains),
//                    Optional.ofNullable(minLength),
//                    Optional.ofNullable(maxLength),
//
//                    Optional.ofNullable(customPredicate),
//                    Optional.ofNullable(customPredicates),
//
//                    this.startsWithMap
//            );
//        }
//    }
//
//    @Override
//    public String toString() {
//        final String FIELD_DELIM = " ";
//        StringBuilder sb = new StringBuilder(0);
//        if(!customPredicate.isEmpty()) {
//            sb.append("customPredicate=" + "TRUE" + FIELD_DELIM);
//        }
//        if(!customPredicates.isEmpty()) {
//            sb.append("customPredicates=" + "TRUE" + FIELD_DELIM);
//        }
//        if(!startsWith.isEmpty()) {
//            //sb.append("startsWith=" + this.startsWith().get() + FIELD_DELIM);
//        }
//        if(!endsWith.isEmpty()) {
//            //sb.append("endsWith=" + this.endsWith().get() + FIELD_DELIM);
//        }
//        if(!contains.isEmpty()) {
//            //sb.append("contains=" + this.contains().get() + FIELD_DELIM);
//        }
//        if(!firstName.isEmpty()) sb.append("firstName=" + this.firstName.get()+ FIELD_DELIM);
//        if(!middleName.isEmpty()) sb.append("middleName=" + this.middleName.get()+ FIELD_DELIM);
//        if(!lastName.isEmpty()) sb.append("lastName=" + this.lastName.get()+ FIELD_DELIM);
//        if(!gender.isEmpty()) sb.append("genders=" + this.gender.get().name() + FIELD_DELIM);
//        if(!genders.isEmpty()) {
//            sb.append("genders=" + genders.get().stream().map(GenderIdentity::name).collect(Collectors.joining()) + FIELD_DELIM);
//        }
//        if(!states.isEmpty()) {
//            sb.append("states=" + String.join("$", USState.names(states.get())) + FIELD_DELIM);
//        }
//        if(!zipCodes.isEmpty()) {
//            //TODO
//            sb.append("zipCodes=" + String.join("$", zipCodes.get()) + FIELD_DELIM);
//        }
//        if(!birthday.isEmpty()) sb.append("birthday=" + this.birthday.get().toString() + FIELD_DELIM);
//        if(!ethnicity.isEmpty()) sb.append("ethnicity=" + this.ethnicity.get().toString() + FIELD_DELIM);
//        if(!domainType.isEmpty()) sb.append("domainType=" + this.domainType.get().toString() + FIELD_DELIM);
//        if(!domain.isEmpty()) sb.append("domain=" + this.domain.get().toString() + FIELD_DELIM);
//        return sb.toString();
//    }
//}
