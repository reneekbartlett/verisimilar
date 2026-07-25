package com.reneekbartlett.verisimilar.core.model;

public record AddressLineTwo (String unitNumber, String unitXtra, UnitType unitType, AddressCategory addressCategory){
    public AddressLineTwo(String unitNumber, UnitType unitType) {
        this(unitNumber, null, unitType, AddressCategory.EMPTY);
    }

    public static AddressLineTwo empty() {
        return new AddressLineTwo(null, null, UnitType.NONE, AddressCategory.EMPTY);
    }

    public static AddressLineTwo placeholder() {
        return new AddressLineTwo("23", "B", UnitType.APARTMENT, AddressCategory.MULTI_FAMILY);
    }

    @Override
    public String toString() {
        final String VALUE_DELIM = " ";
        StringBuilder sb = new StringBuilder(0);
        if(this.unitNumber != null) sb.append(this.unitNumber).append(VALUE_DELIM);
        if(this.unitXtra != null) sb.append(this.unitXtra).append(VALUE_DELIM);
        //if(addressCategory != null) sb.append(this.addressCategory.getLabel());
        //if(unitType != null) sb.append(this.unitType.getLabel());
        return sb.toString();
    }
}
