package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;

/***
 * 
 */
public record StreetNameDatasetKey(
        String id, 
        EnumSet<USState> states
) implements DatasetKey {

    private static final String KEY_ID = "STREET_NAME"; //TemplateField.STREET_NAME.getLabel()

    public StreetNameDatasetKey(EnumSet<USState> state) {
        this(KEY_ID, state);
    }

    public StreetNameDatasetKey(USState state) {
        this(KEY_ID, EnumSet.of(state));
    }

    @Override
    public Set<TemplateField> fields(){
        return EnumSet.of(TemplateField.STATE);
    }

    public static StreetNameDatasetKey defaults() {
        return new StreetNameDatasetKey(KEY_ID, null);
    }

    public static StreetNameDatasetKey fromContext(DatasetResolutionContext ctx) {
        EnumSet<USState> states = ctx.states().orElse(USState.defaultDatasets());
        return new StreetNameDatasetKey(KEY_ID, states);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0).append(id);
        if(states != null) sb.append("$").append(states.stream().map(USState::getPlaceholder).collect(Collectors.joining("|")));
        return sb.toString();
    }
}
