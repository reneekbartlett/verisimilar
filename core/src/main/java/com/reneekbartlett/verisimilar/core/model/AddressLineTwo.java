package com.reneekbartlett.verisimilar.core.model;

/***
 * Combined field containing:
 *   Address2 (String), 
 *   UnitNumber (UNIT_NUMBER), 
 *   UnitXtr (String) 
 *   UnitType (UNIT_TYPE)
 *   AddressCategory (ADDRESS_CATEGORY)
 */
public record AddressLineTwo (String address2, String unitNumber, String unitXtra, UnitType unitType, AddressCategory addressCategory){

    public AddressLineTwo(String unitNumber, UnitType unitType) {
        this(
            unitType.getLabel() + " " + unitNumber + " ",
            unitNumber,
            null, 
            unitType, 
            AddressCategory.EMPTY
        );
    }

    @Override
    public String address2() {
        if(address2 == null) {
            return unitType.getLabel() + " " + unitNumber;
        }
        return address2;
    }

    public static AddressLineTwo empty() {
        return new AddressLineTwo(null, null, null, UnitType.EMPTY, AddressCategory.EMPTY);
    }

    public static AddressLineTwo placeholder() {
        return new AddressLineTwo("APT 23B", "23", "B", UnitType.APARTMENT, AddressCategory.MULTI_FAMILY);
    }

    @Override
    public String toString() {
        final String VALUE_DELIM = " ";
        StringBuilder sb = new StringBuilder(0);
        if(unitType != null) sb.append(this.unitType.getLabel()).append(VALUE_DELIM);
        if(this.unitNumber != null) sb.append(this.unitNumber).append(VALUE_DELIM);
        if(this.unitXtra != null) sb.append(" " + this.unitXtra);
        //if(addressCategory != null) sb.append(this.addressCategory.getLabel());
        return sb.toString().trim();
    }
}
