package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.Set;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.USState;

/***
 * 
 */
public record FirstNameDatasetKey(
        String id,
        Set<GenderIdentity> genders,
        Integer year,
        USState state,
        Ethnicity ethnicity
) implements DatasetKey {

    public static final String KEY_ID = "FIRSTNAME";

    public static FirstNameDatasetKey defaults() {
        return new FirstNameDatasetKey(KEY_ID, null, null, null, null);
    }

    public FirstNameDatasetKey(GenderIdentity gender, Ethnicity ethnicity, USState state) {
        this(KEY_ID, Set.of(gender), null, null, ethnicity);
    }
    
    public FirstNameDatasetKey(Set<GenderIdentity> genders) {
        this(KEY_ID, genders, null, null, null);
    }

    public FirstNameDatasetKey(GenderIdentity gender, Ethnicity ethnicity) {
        this(KEY_ID, Set.of(gender), null, null, ethnicity);
    }

    public FirstNameDatasetKey(GenderIdentity gender, USState state) {
        this(KEY_ID, Set.of(gender), null, state, null);
    }

    public FirstNameDatasetKey(Integer year) {
        this(KEY_ID, null, year, null, null);
    }

    @Override
    public String toString() {
        StringBuilder sb =  new StringBuilder(0).append(id);
        //if(gender != null) sb.append("$").append(gender.getLabel());
        if(genders != null) sb.append("$").append(genders.stream().map(Enum::name).collect(Collectors.joining("|")));
        if(year != null) sb.append("$").append(year);
        if(ethnicity != null) sb.append("$").append(ethnicity.getLabel());
        return sb.toString();
    }
}
