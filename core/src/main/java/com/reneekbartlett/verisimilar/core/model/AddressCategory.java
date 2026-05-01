package com.reneekbartlett.verisimilar.core.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/***
 * Single Family Residential (SFR): Detached homes without shared common interest (no HOA).
* Condominium/Townhouse: Attached or detached homes with a common interest development (CID) or HOA.
* Multi-Family Residential: Buildings with two, three, or four units (duplex, triplex, fourplex).
* Manufactured/Mobile Home: Homes built off-site, often specified as "in park".
* Residential Income: Income-producing properties (often larger than 4 units).

* Common MLS Property Subtypes:
* Detached: Free-standing, single-family structure.
* Attached: Connected to another unit (e.g., townhouses, rowhouses).
* Duplex/Triplex/Fourplex: Attached units sharing walls, often with separate ownership structures.
* Garden/Mid-Rise/High-Rise: Defines the number of stories in an apartment/condo building.
*/
public enum AddressCategory {

    SINGLE_FAMILY("Single Family Residential (SFR)", 0.0001),
    MULTI_FAMILY("Multi Family", 0.0001),
    PO_BOX("", 0.0001);

    private final String label;
    private final double weight;

    private AddressCategory(String label, double weight) {
        this.label = label;
        this.weight = weight;
    }

    public String getLabel() {
        return label;
    }

    public double getWeight() {
        return weight;
    }

    public static EnumSet<AddressCategory> defaultDatasets(){
        return EnumSet.of(SINGLE_FAMILY, MULTI_FAMILY);
    }

    public static Map<AddressCategory, Double> defaultMap() {
        Map<AddressCategory, Double> defaultMap = HashMap.newHashMap(defaultDatasets().size());
        for(AddressCategory category : defaultDatasets()) {
            defaultMap.put(category, category.getWeight());
        }
        return defaultMap;
    }

    public static AddressCategory fromLabel(String label) {
        for (AddressCategory cat : values()) {
            if (cat.getLabel().equalsIgnoreCase(label)) {
                return cat;
            }
        }
        //throw new IllegalArgumentException("Invalid state abbreviation: " + abbr);
        return null;
    }
}
