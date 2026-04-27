package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.Set;

import com.reneekbartlett.verisimilar.core.model.USRegion;
import com.reneekbartlett.verisimilar.core.model.USState;

public record CityStateZipDatasetKey(
        String id,
        Set<USState> states,
        USRegion region,
        Set<String> zipCodes
) implements DatasetKey {

    public static final String KEY_ID = "CITYSTATEZIP";

    public static CityStateZipDatasetKey defaults() {
        return new CityStateZipDatasetKey(KEY_ID, null, null, null);
    }

    public CityStateZipDatasetKey(Set<USState> states) {
        this(KEY_ID, states, null, null);
    }

    public CityStateZipDatasetKey(Set<USState> states, Set<String> zipCodes) {
        this(KEY_ID, states, null, zipCodes);
    }

    public CityStateZipDatasetKey(USRegion region) {
        this(KEY_ID, null, region, null);
    }

    @Override
    public String toString() {
        StringBuilder sb =  new StringBuilder(0).append(id);
        if(states != null) sb.append("$").append(String.join("$", USState.names(states)));
        if(zipCodes != null) sb.append("$").append(String.join("$", zipCodes));
        if(region != null) sb.append("$").append(region.name());
        return sb.toString();
    }
}
