package com.reneekbartlett.verisimilar.core.util;

public class FieldValidationUtils {

    public static boolean validateState(String state) {
        if(state.length() > 2) {
            return false;
        }
        return true;
    }

    public static boolean validateAreaCode(Integer areaCode) {
        if (areaCode == null || areaCode < 200 || areaCode > 999) {
            throw new IllegalArgumentException("Area code must be between 200 and 999.");
        }
        return true;
    }

    public static boolean validateExchangeCode(String exchangeCode) {
        if (exchangeCode != null) {
            Integer startsWith = Integer.valueOf(exchangeCode.indexOf(0));
            if (startsWith < 2 || startsWith > 9) throw new IllegalArgumentException("Starting digit must be between 2 and 9.");
        }
        return true;
    }

    public static boolean validatePhoneStr(String phoneStr) {
        if (phoneStr == null || !phoneStr.matches("\\d{10}")) {
            //LOGGER.debug(phoneStr);
            throw new IllegalArgumentException("Input must be exactly 10 digits.");
        }
        return true;
    }
}
