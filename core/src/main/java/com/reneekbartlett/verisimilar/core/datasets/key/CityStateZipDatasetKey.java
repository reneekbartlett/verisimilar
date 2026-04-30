package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.Set;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.model.USRegion;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;

public record CityStateZipDatasetKey(
        String id,
        Set<USState> states,
        Set<USRegion> regions
) implements DatasetKey {

    public static final String KEY_ID = "CITYSTATEZIP";

    public static CityStateZipDatasetKey defaults() {
        return new CityStateZipDatasetKey(KEY_ID, null, null);
    }

    public CityStateZipDatasetKey(Set<USState> states) {
        this(KEY_ID, states, USRegion.defaultDatasets());
    }

    public CityStateZipDatasetKey(USRegion region) {
        this(KEY_ID, USState.defaultDatasets(), Set.of(region));
    }
    
    public static CityStateZipDatasetKey fromContext(DatasetResolutionContext ctx) {
        //TODO: Use Region OR States.
        Set<USState> states = ctx.states().orElse(USState.defaultDatasets());
        Set<USRegion> regions = ctx.regions().orElse(USRegion.defaultDatasets());
        return new CityStateZipDatasetKey(KEY_ID, states, regions);
    }

    @Override
    public String toString() {
        StringBuilder sb =  new StringBuilder(0).append(id);
        if(states != null) sb.append("$").append(states.stream().map(USState::getPlaceholder).collect(Collectors.joining("|")));
        if(regions != null) sb.append("$").append(regions.stream().map(USRegion::getPlaceholder).collect(Collectors.joining("|")));
        return sb.toString();
    }
}
