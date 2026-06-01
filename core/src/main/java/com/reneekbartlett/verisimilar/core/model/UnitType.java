package com.reneekbartlett.verisimilar.core.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/***
 * REAR / FRONT — Frequently used in Brooklyn and Queens for multi-building lots (e.g., a tenement building with a front and rear house on the same property).
 * PH (Penthouse) — Top-floor luxury units, often associated with high-rises or luxury co-ops/condos.
 * TH (Townhouse) — Individual multi-level units usually within larger developments.
 * BSMT
 * https://public-dhhs.ne.gov/nfocus/HowDoI/howdoi/usps_address_unit_types.htm
 */
public enum UnitType {
    APARTMENT("Apt", 0.0001, "APT"),
    UNIT("Unit", 0.0001, "U"),
    ROOM("RM", 0.0001, "ROOM"),
    SUITE("STE", 0.0001),
    FLOOR("FL", 0.0001, "FLR"),
    BASEMENT("Bsmt", 0.0001),
    BUILDING("Bldg", 0.0001),

    PENTHOUSE("Penthouse", 0.0001, "PH"),
    TOWNHOUSE("TH", 0.0001),
    STUDIO("Studio", 0.0001),
    LOFT("Loft", 0.0001),
    REAR("REAR", 0.0001),
    FRONT("FRONT", 0.0001),
    UPPER("UPPER", 0.0001),
    // REAR, FRONT, TOWNHOUSE/TH
    UNNOWN("", 0.0000);

    private final String label;
    private final double weight;
    private final String[] searchStr;

    private UnitType(String label, double weight, String... searchStr) {
        this.label = label;
        this.weight = weight;
        this.searchStr = searchStr;
    }

    public String getLabel() {
        return label;
    }

    public double getWeight() {
        return weight;
    }

    public String[] getSearchStrings() {
        return searchStr;
    }

    public static EnumSet<UnitType> defaultDatasets(){
        return EnumSet.allOf(UnitType.class);
    }

    public static Map<UnitType, Double> defaultMap() {
        Map<UnitType, Double> defaultMap = HashMap.newHashMap(defaultDatasets().size());
        for(UnitType unitType : defaultDatasets()) {
            defaultMap.put(unitType, unitType.getWeight());
        }
        return defaultMap;
    }

    public static UnitType fromLabel(String label) {
        for (UnitType unitType : EnumSet.allOf(UnitType.class)) {
            if (unitType.name().equalsIgnoreCase(label) || unitType.getLabel().equalsIgnoreCase(label)) {
                return unitType;
            }
        }
        return null;
    }

    public static EnumSet<UnitType> convertToEnumSet(Set<String> unitTypes){
        EnumSet<UnitType> unitTypeEnumSet = EnumSet.noneOf(UnitType.class);
        for(String unitType : unitTypes) {
            UnitType t = UnitType.fromLabel(unitType);
            if(t != null) {
                unitTypeEnumSet.add(t);
            }
        }
        return unitTypeEnumSet;
    }
}
