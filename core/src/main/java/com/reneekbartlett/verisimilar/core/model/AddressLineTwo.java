package com.reneekbartlett.verisimilar.core.model;

public record AddressLineTwo (String unitNumber, String unitXtra, AddressCategory addressCategory, UnitType unitType){
    @Override
    public String toString() {
        final String VALUE_DELIM = " ";
        StringBuilder sb = new StringBuilder(0);
        sb.append(this.unitNumber).append(VALUE_DELIM);
        sb.append(this.unitXtra).append(VALUE_DELIM);
        if(addressCategory != null) sb.append(this.addressCategory.getLabel());
        if(unitType != null) sb.append(this.unitType.getLabel());
        return sb.toString().toUpperCase();
    }
}
