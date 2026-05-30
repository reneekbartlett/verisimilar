package com.reneekbartlett.verisimilar.core.selector.filter;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.FullName;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.PersonRecord;
import com.reneekbartlett.verisimilar.core.model.PostalAddress;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.USRegion;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.model.UsernameType;

/***
 * 
 */
public record SelectionFilter(
        Optional<String> firstName,
        Optional<String> middleName,
        Optional<String> lastName,

        Optional<String> nickName,

        Optional<GenderIdentity> gender,
        Optional<Set<GenderIdentity>> genders,

        Optional<LocalDate> birthday,
        Optional<Integer> minYear,
        Optional<Integer> maxYear,

        Optional<PostalAddress> postalAddress,

        Optional<String> streetName,
        Optional<String> streetSuffix,

        Optional<String> city,

        Optional<USState> state,
        Optional<Set<USState>> states,

        Optional<Set<String>> zipCodes,

        Optional<USRegion> region,

        Optional<Ethnicity> ethnicity,

        Optional<DomainType> domainType,
        Optional<String> domain,

        Optional<UsernameType> usernameType,
        Optional<String> username,

        Optional<String> areaCode,

        Optional<SelectionPredicate<String>> customPredicate,
        Optional<Set<SelectionPredicate<String>>> customPredicates,

        Map<TemplateField, String> startsWithMap,
        Map<TemplateField, String> endsWithMap,
        Map<TemplateField, String> equalToMap,
        Map<TemplateField, String> containsMap,
        Map<TemplateField, Set<String>> inMap,
        Map<TemplateField, Set<?>> inEnumMap
) {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelectionFilter.class);

    public SelectionFilter {
        // TODO:  Check if these optional checks are necessary
        firstName = firstName == null? Optional.empty() : firstName;
        middleName = middleName == null? Optional.empty() : middleName;
        lastName = lastName == null? Optional.empty() : lastName;

        nickName = nickName == null? Optional.empty() : nickName;

        gender = gender == null ? Optional.empty() : gender;
        genders = genders == null ? Optional.empty() : genders;

        birthday = (birthday == null) ? Optional.empty() : birthday;
        minYear = (minYear == null) ? Optional.empty() : minYear;
        maxYear = (maxYear == null) ? Optional.empty() : maxYear;

        postalAddress = postalAddress == null ? Optional.empty() : postalAddress;

        streetName = streetName == null ? Optional.empty() : streetName;
        streetSuffix = streetSuffix == null ? Optional.empty() : streetSuffix;

        city = city == null ? Optional.empty() : city;

        state = state == null ? Optional.empty() : state;
        states = states == null ? Optional.empty() : states;

        zipCodes = zipCodes == null ? Optional.empty() : zipCodes;

        region = region == null ? Optional.empty() : region;
        ethnicity = ethnicity == null ? Optional.empty() : ethnicity;

        areaCode = areaCode == null ? Optional.empty() : areaCode;

        domainType = domainType == null ? Optional.empty() : domainType;
        domain = domain == null ? Optional.empty() : domain;

        usernameType = usernameType == null ? Optional.empty() : usernameType;
        username = username == null ? Optional.empty() : username;

        customPredicate = customPredicate == null ? Optional.empty() : customPredicate;
        customPredicates = customPredicates == null ? Optional.empty() : customPredicates;
    }

    public static Builder toBuilder(SelectionFilter filter) {
        return new Builder(filter);
    }

    public Builder toBuilder() {
        // todo: clone?
        return new Builder(this);
    }

    public PersonRecord getPersonRecord() {
        FullName fullName = new FullName(firstName.orElse(""), middleName.orElse(""), lastName.orElse(""));
        GenderIdentity genderIdentity = gender.orElse(GenderIdentity.GENDER_UNSPECIFIED);
        return new PersonRecord(
                fullName,
                genderIdentity,
                birthday.orElse(null)
        );
    }

    public Map<TemplateField, Object> getResolvedValues() {
        Map<TemplateField, Object> values = new HashMap<>();
        if(!firstName.isEmpty()) values.put(TemplateField.FIRST_NAME, firstName.get());
        if(!middleName.isEmpty()) values.put(TemplateField.MIDDLE_NAME, middleName.get());
        if(!lastName.isEmpty()) values.put(TemplateField.LAST_NAME, lastName.get());
        if(!birthday.isEmpty()) {
            values.put(TemplateField.BIRTHDAY, birthday.get());
        }

        if(!domain.isEmpty()) values.put(TemplateField.DOMAIN, domain.get());
        if(!gender.isEmpty()) values.put(TemplateField.GENDER_IDENTITY, gender.get());
        return values;
    }

    public boolean isEmpty() {
        return customPredicate.isEmpty() && customPredicates.isEmpty()
                && firstName.isEmpty() && middleName.isEmpty() && lastName.isEmpty()
                && nickName.isEmpty()
                && gender.isEmpty()
                && birthday.isEmpty()
                && minYear.isEmpty()
                && maxYear.isEmpty()
                && streetName.isEmpty()
                && city.isEmpty()
                && states.isEmpty()
                && state.isEmpty()
                && zipCodes.isEmpty()
                && region.isEmpty()
                && ethnicity.isEmpty()
                && domainType.isEmpty()
                && domain.isEmpty()
                && startsWithMap.isEmpty()
                && endsWithMap.isEmpty()
                && equalToMap.isEmpty()
                && containsMap.isEmpty()
                && inMap.isEmpty();
    }

    public static SelectionFilter empty() {
        return new SelectionFilter(
                // FullName
                Optional.empty(), Optional.empty(), Optional.empty(),

                Optional.empty(), // nickName

                Optional.empty(), // Gender
                Optional.empty(), // Genders

                Optional.empty(), // Birthday
                Optional.empty(), Optional.empty(), // MinYear, MaxYear

                Optional.empty(), // PostalAddress
                Optional.empty(), // streetName
                Optional.empty(), // streetSuffix
                Optional.empty(), // city

                Optional.empty(), // state
                Optional.empty(), // states

                Optional.empty(), // zipCodes
                Optional.empty(), // region
                Optional.empty(), // ethnicity

                Optional.empty(), // domainType
                Optional.empty(), // domain

                Optional.empty(), // usernameType
                Optional.empty(), // username

                Optional.empty(), // areaCode

                Optional.empty(), // customPredicate
                Optional.empty(), // customPredicates

                HashMap.newHashMap(0),
                HashMap.newHashMap(0),
                HashMap.newHashMap(0),
                HashMap.newHashMap(0),
                HashMap.newHashMap(0),
                HashMap.newHashMap(0) // inEnumMap
        );
    }

    // ------------------------------------------------------------
    // Builder
    // ------------------------------------------------------------
    //
    // TODO:  Decide if I want to keep individual fields are just use the maps (i.e. equalToMap, etc.)
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String firstName;
        private String middleName;
        private String lastName;

        private String nickName;

        private GenderIdentity gender;
        private Set<GenderIdentity> genders;

        private LocalDate birthday;
        private Integer minYear;
        private Integer maxYear;

        private PostalAddress postalAddress;
        private String streetName;
        private String streetSuffix;
        private String city;

        private USState state;
        private Set<USState> states;

        private Set<String> zipCodes = HashSet.newHashSet(0);

        private USRegion region;
        private Ethnicity ethnicity;
        private DomainType domainType;
        private String domain;

        private UsernameType usernameType;
        private String username;

        private String areaCode;

        protected SelectionPredicate<String> customPredicate;
        protected Set<SelectionPredicate<String>> customPredicates = HashSet.newHashSet(0);

        protected Map<TemplateField, String> startsWithMap = HashMap.newHashMap(0);
        protected Map<TemplateField, String> endsWithMap = HashMap.newHashMap(0);

        protected Map<TemplateField, String> containsMap = HashMap.newHashMap(0);

        protected Map<TemplateField, String> equalToMap = HashMap.newHashMap(0);
        protected Map<TemplateField, Set<String>> inMap = HashMap.newHashMap(0);

        public Builder() {
            //
        }

        // Maps a Class type to a Set of that specific type
        private final Map<TemplateField, Set<?>> inEnumMap = new HashMap<>();

        @SuppressWarnings("unchecked")
        public <T> Set<T> getSet(TemplateField field, Class<T> type) {
            return (Set<T>) inEnumMap.computeIfAbsent(field, k -> new HashSet<T>());
        }

        /***
         * Copy over some existing elements.
         */
        public Builder(SelectionFilter filter) {
            this.gender = filter.gender.orElse(null);
            this.ethnicity = filter.ethnicity.orElse(null);

            this.firstName = filter.firstName.orElse(null);
            this.middleName = filter.middleName.orElse(null);
            this.lastName = filter.lastName.orElse(null);
            this.nickName = filter.nickName.orElse(null);
            this.birthday = filter.birthday.orElse(null);

            // todo: postalAddress?
            this.streetName = filter.streetName.orElse(null);
            this.streetSuffix = filter.streetSuffix.orElse(null);

            this.city = filter.city.orElse(null);

            this.state = filter.state.orElse(null);
            this.states = filter.states.orElse(null);

            this.zipCodes = filter.zipCodes.orElse(null);

            this.region = filter.region.orElse(null);

            this.startsWithMap = filter.startsWithMap();
            this.endsWithMap = filter.endsWithMap();
            this.containsMap = filter.containsMap();

            // TODO:  I don't think these need to get copied...
            this.equalToMap = filter.equalToMap();
            //this.inMap = filter.inMap();

            LOGGER.debug(this.toString());
        }

        public Builder firstName(String value) {
            this.firstName = value;
            this.equalToMap.put(TemplateField.FIRST_NAME, value);
            return this;
        }

        public Builder middleName(String value) {
            this.middleName = value;
            this.equalToMap.put(TemplateField.MIDDLE_NAME, value);
            return this;
        }

        public Builder lastName(String value) {
            this.lastName = value;
            this.equalToMap.put(TemplateField.LAST_NAME, value);
            return this;
        }

        public Builder nickName(String value) {
            this.nickName = value;
            this.equalToMap.put(TemplateField.NICKNAME, value);
            return this;
        }

        public Builder gender(GenderIdentity value) {
            this.gender = value;
            this.equalToMap.put(TemplateField.GENDER_IDENTITY, value.getLabel());
            return this;
        }

        public Builder genders(EnumSet<GenderIdentity> genders) {
            this.genders = genders;
            //this.inMap.put(TemplateField.GENDER_IDENTITY, genders);
            this.inEnumMap.put(TemplateField.GENDER_IDENTITY, genders);
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

        public Builder postalAddress(PostalAddress value) {
            this.postalAddress = value;

            this.state = USState.fromAbbreviation(value.state());
            this.equalToMap.put(TemplateField.STATE, value.state());

            this.zipCode(value.zip());
            this.equalToMap.put(TemplateField.ZIP_CODE, value.zip());

            return this;
        }

        public Builder streetName(String value) {
            this.streetName = value;
            this.equalToMap.put(TemplateField.STREET_NAME, value);
            return this;
        }

        public Builder streetSuffix(String value) {
            this.streetSuffix = value;
            this.equalToMap.put(TemplateField.STREET_SUFFIX, value);
            return this;
        }

        public Builder city(String value) {
            this.city = value;
            this.equalToMap.put(TemplateField.CITY, value);
            return this;
        }

        // TODO:  Check handling of abbreviations/full name.  Maybe switch to customPredicate.
        public Builder state(USState value) {
            if(value != null) {
                this.state = value;
                this.equalToMap.put(TemplateField.STATE, value.name());
            }
            return this;
        }

        // TODO:  check if already set?  fix the toBuilder() clone prob.
        public Builder states(Set<USState> values) {
            if(states == null) {
                this.states = values;
                this.inEnumMap.put(TemplateField.STATE, values);
                Set<String> stateNames = new HashSet<>();
                for(USState state : values) {
                    stateNames.add("$"+state.name()+"$");
                }
                SelectionPredicate<String> p = (val) -> stateNames.stream().anyMatch(val::contains);
                this.customPredicates.add(p);
            }
            return this;
        }

        public Builder zipCode(String value) {
            this.zipCodes = Set.of(value);
            this.equalToMap.put(TemplateField.ZIP_CODE, value);
            return this;
        }

        public Builder zipCodes(Set<String> values) {
            if(zipCodes.isEmpty()) {
                this.zipCodes = values;
                // TODO:  Also filter by state?
                Set<String> zipCodeValues = new HashSet<>();
                for(String zipCode : values) {
                    zipCodeValues.add("$"+zipCode);
                }
                SelectionPredicate<String> p = (val) -> zipCodeValues.stream().anyMatch(val::contains);
                this.customPredicates.add(p);
            }
            return this;
        }

        public Builder region(USRegion value) {
            this.region = value;
            this.equalToMap.put(TemplateField.REGION, value.getRegionName());
            return this;
        }

        // TODO:  Keep 2 regions options? inMap or predicate for enums?
        public Builder regions(Set<String> values) {
            this.inMap.put(TemplateField.REGION, values);
            return this;
        }

        public Builder regions(EnumSet<USRegion> values) {
            this.inEnumMap.put(TemplateField.REGION, values);

            Set<String> regionNames = new HashSet<>();
            for(USRegion region : values) {
                regionNames.add(region.name());
            }
            SelectionPredicate<String> p = (val) -> regionNames.stream().anyMatch(val::equalsIgnoreCase);
            this.customPredicates.add(p);
            return this;
        }

        public Builder ethnicity(Ethnicity value) {
            this.ethnicity = value;
            this.equalToMap.put(TemplateField.ETHNICITY, value.getPlaceholder());
            return this;
        }

        public Builder ethnicities(EnumSet<Ethnicity> values) {
            this.inEnumMap.put(TemplateField.ETHNICITY, values);

            Set<String> ethnicityNames = new HashSet<>();
            for(Ethnicity ethnicity : values) {
                ethnicityNames.add(ethnicity.name());
            }
            SelectionPredicate<String> p = (val) -> ethnicityNames.stream().anyMatch(val::equalsIgnoreCase);
            this.customPredicates.add(p);
            //this.inMap.put(TemplateField.Ethnicity, ethnicityNames);
            return this;
        }

        public Builder domainType(DomainType value) {
            this.domainType = value;
            this.equalToMap.put(TemplateField.DOMAIN_TYPE, value.getPlaceholder());
            return this;
        }

        public Builder domain(String value) {
            this.domain = StringUtils.deleteWhitespace(value);
            this.equalToMap.put(TemplateField.DOMAIN, this.domain);
            return this;
        }

        public Builder domains(Set<String> values) {
            this.inMap.put(TemplateField.DOMAIN, values);
            return this;
        }

        public Builder usernameType(UsernameType value) {
            this.usernameType = value;
            this.equalToMap.put(TemplateField.USERNAME_TYPE, value.getPlaceholder());
            return this;
        }

        public Builder username(String value) {
            if(value != null && !value.isBlank()) {
                // TODO:  Format/validate.  i.e. trim, remove spaces
                this.username = StringUtils.deleteWhitespace(value);
                this.equalToMap.put(TemplateField.USERNAME, this.username);
            }
            return this;
        }

        public Builder areaCode(String value) {
            if(value != null && !value.isBlank()) {
                this.areaCode = StringUtils.deleteWhitespace(value);
                this.equalToMap.put(TemplateField.AREA_CODE, this.areaCode);
            }
            return this;
        }

        public Builder startsWith(String value, TemplateField field) {
            if(value != null && !value.isBlank()) {
                this.startsWithMap.put(field, value);
            }
            return this;
        }

        public Builder endsWith(String value, TemplateField field) {
            this.endsWithMap.put(field, value);
            return this;
        }

        public Builder contains(String value, TemplateField field) {
            this.containsMap.put(field, value);
            return this;
        }

        protected Builder equalTo(String value, TemplateField field) {
            // TODO:  Check
            this.equalToMap.put(field, value);
            return this;
        }

        public Builder in(Set<String> values, TemplateField field) {
            this.inMap.put(field, values);
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
                    Optional.ofNullable(nickName),

                    Optional.ofNullable(gender),
                    Optional.ofNullable(genders),

                    Optional.ofNullable(birthday),
                    Optional.ofNullable(minYear),
                    Optional.ofNullable(maxYear),

                    Optional.ofNullable(postalAddress),
                    Optional.ofNullable(streetName),
                    Optional.ofNullable(streetSuffix),
                    Optional.ofNullable(city),

                    // TODO: State take precedence over states
                    Optional.ofNullable(state),
                    Optional.ofNullable(states),

                    Optional.ofNullable(zipCodes),
                    Optional.ofNullable(region),
                    Optional.ofNullable(ethnicity),
                    Optional.ofNullable(domainType),
                    Optional.ofNullable(domain),
                    Optional.ofNullable(usernameType),
                    Optional.ofNullable(username),

                    Optional.ofNullable(areaCode),

                    Optional.ofNullable(customPredicate),
                    Optional.ofNullable(customPredicates),

                    startsWithMap,
                    endsWithMap,
                    equalToMap,
                    containsMap,
                    inMap,
                    inEnumMap
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
        if(!startsWithMap.isEmpty()) {
            sb.append("startsWithMap.size()=" + this.startsWithMap.size() + FIELD_DELIM);
        }
        if(!endsWithMap.isEmpty()) {
            sb.append("endsWithMap.size()=" + this.endsWithMap.size() + FIELD_DELIM);
        }
        if(!containsMap.isEmpty()) {
            sb.append("containsMap.size()=" + this.containsMap.size() + FIELD_DELIM);
        }
        if(!equalToMap.isEmpty()) {
            sb.append("equalToMap.size()=" + this.equalToMap.size() + FIELD_DELIM);
        }
        if(!inMap.isEmpty()) {
            sb.append("inMap.size()=" + this.inMap.size() + FIELD_DELIM);
        }
        if(!firstName.isEmpty()) sb.append("firstName=" + this.firstName.get()+ FIELD_DELIM);
        if(!middleName.isEmpty()) sb.append("middleName=" + this.middleName.get()+ FIELD_DELIM);
        if(!lastName.isEmpty()) sb.append("lastName=" + this.lastName.get()+ FIELD_DELIM);
        if(!nickName.isEmpty()) sb.append("nickName=" + this.nickName.get()+ FIELD_DELIM);
        if(!gender.isEmpty()) sb.append("gender=" + this.gender.get().name() + FIELD_DELIM);
        if(!genders.isEmpty()) {
            //sb.append("genders=" + Arrays.toString(Stream.of(genders.get()).map(GenderIdentity::name).toArray(String[]::new)) + FIELD_DELIM);
            sb.append("genders=" + genders.get().stream().map(GenderIdentity::name).collect(Collectors.joining()) + FIELD_DELIM);
        }
        //if(!postalAddress.isEmpty()) {
        //    sb.append("postalAddress=" + postalAddress.toString()) + FIELD_DELIM);
        //}
        if(!state.isEmpty()) {
            sb.append("state=" + state.get().name() + FIELD_DELIM);
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
