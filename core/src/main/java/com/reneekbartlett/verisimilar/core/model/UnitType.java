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
 * 
 * https://pe.usps.com/text/pub28/28apc_003.htm
 * https://public-dhhs.ne.gov/nfocus/HowDoI/howdoi/usps_address_unit_types.htm
 */
public enum UnitType implements WeightedEnumData {
    APARTMENT("APT", 0.1000, "APT", "APARTMENT"),
    UNIT("UNIT", 0.0001, "UNIT", "U", "UNT"),
    ROOM("RM", 0.0001, "ROOM", "RM"),
    SUITE("STE", 0.0001, "SUITE", "STE"),
    FLOOR("FL", 0.0001, "FLR", "FL", "FLOOR"),
    BASEMENT("BSMT", 0.0001, "BASEMENT", "BSMT", "BASEMNT"),
    BUILDING("BLDG", 0.0001, "BUILDING", "BLDG"),

    PENTHOUSE("PH", 0.0001, "PH", "PENTHOUSE"),
    TOWNHOUSE("TH", 0.0001),
    STUDIO("Studio", 0.0001),
    LOFT("Loft", 0.0001),
    REAR("REAR", 0.0001),
    FRONT("FRONT", 0.0001),
    UPPER("UPPER", 0.0001),

    DEPARTMENT("DEPT", 0.0000, "DEPARTMENT","DEPT"),

    HANGER("HNGR", 0.0000, "HANGER","HNGR"),
    KEY("KEY", 0.0000, "KEY","KY"),
    LOBBY("LBBY", 0.0000, "LOBBY","LBBY"),
    LOT("LOT", 0.0000, "LOT","LT"),
    LOWER("LOWR", 0.0000, "LOWER","LOWR", "LWR"),
    OFFICE("OFC", 0.0000, "OFFICE","OFC", "OFFC"),

    PIER("PIER", 0.0000, "PIER","PR"),

    SIDE("SIDE", 0.0000, "SIDE","SD"),
    SLIP("SLIP", 0.0000, "SLIP","SLP"),
    SPACE("SPC", 0.0000, "SPACE","SPC"),
    STOP("STOP", 0.0000, "STOP","STP"),

    TRAILER("TRLR", 0.0000, "TRAILER","TRLR"),

    NONE("", 0.0000, "", "NONE", "N/A", "BLANK"),

    EMPTY("", 0.0000, "EMPTY"),
    UNKNOWN("", 0.0000, "UNKNOWN");

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

    public static EnumSet<UnitType> getWeightedEnumDataSet() {
        return defaultDatasets();
    }
}
