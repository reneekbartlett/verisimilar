package com.reneekbartlett.verisimilar.core.generator;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.reneekbartlett.verisimilar.core.datasets.key.AreaCodeDatasetKey;
import com.reneekbartlett.verisimilar.core.generator.api.AbstractValueGenerator;
import com.reneekbartlett.verisimilar.core.model.PhoneNumber;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.AreaCodeSelectionEngine;

/***
 * Map<String, String[]> AREACODES_BY_STATE ResourceMapLoader.loadArrayMap("/phone_areacode_bystate_us.txt");
 * EXCHANGE_CODES_BY_AREACODE
 * PHONE_NUMBER_TYPE
 */
public class PhoneNumberGenerator extends AbstractValueGenerator<PhoneNumber> {

    // TODO: EXCHANGE_CODES_BY_AREACODE
    // TODO: PHONE_NUMBER_TYPE

    private final AreaCodeSelectionEngine areaCodeSelector;

    public PhoneNumberGenerator(AreaCodeSelectionEngine areaCodeSelector) {
        this.areaCodeSelector = areaCodeSelector;
    }

    @Override
    protected PhoneNumber generateValue(DatasetResolutionContext ctx, SelectionFilter criteria) {
        return generatePhoneNumber(ctx, criteria);
    }

    @Override
    protected Class<PhoneNumber> valueType() {
        return PhoneNumber.class;
    }

    private PhoneNumber generatePhoneNumber(DatasetResolutionContext ctx, SelectionFilter criteria) {
        String areaCode = generateAreaCode(ctx, criteria);
        String exchangeCode = generateExchangeCode(ctx, criteria);
        String lineNumber = generateLineNumber(ctx, criteria);

        return new PhoneNumber(areaCode, exchangeCode, lineNumber);
    }

    private String generateAreaCode(DatasetResolutionContext ctx, SelectionFilter filter) {
        Set<USState> usStates = filter.states().orElse(null);
        AreaCodeDatasetKey areaCodeKey = new AreaCodeDatasetKey(usStates);

        String areaCode = areaCodeSelector.select(areaCodeKey, filter);
        //LOGGER.debug("selected:{}", areaCode);
        return areaCode;
    }

    // TODO:  Configure option to just get random for backup cases?
    @SuppressWarnings("unused")
    private String getRandomAreaCode() {
        int areaCode = ThreadLocalRandom.current().nextInt(200, 999+1);
        return String.valueOf(areaCode);
    }
    
    // TODO Configure option to just get random for backup cases?
    @SuppressWarnings("unused")
    private USState getRandomState() {
        USState[] options = USState.values();
        return options[ThreadLocalRandom.current().nextInt(options.length)];
    }

    private String generateExchangeCode(DatasetResolutionContext ctx, SelectionFilter criteria) {
        // TODO:  Cleanup?
        //Integer startsWith = null;
        //if (startsWith != null) {
        //    if (startsWith < 2 || startsWith > 9) throw new IllegalArgumentException("Starting digit must be between 2 and 9.");
        //    firstDigit = startsWith;
        //}

        // Generate exchange code (NXX)
        int firstDigit = ThreadLocalRandom.current().nextInt(2, 10); // 2–9
        int secondDigit = ThreadLocalRandom.current().nextInt(0, 10);
        int thirdDigit  = ThreadLocalRandom.current().nextInt(0, 10);

        int exchangeCode = firstDigit * 100 + secondDigit * 10 + thirdDigit;

        return formatExchangeCode(exchangeCode);
    }

    

    private String generateLineNumber(DatasetResolutionContext ctx, SelectionFilter criteria) {
        // Line number (0000–9999)
        int lineNumber = ThreadLocalRandom.current().nextInt(0, 10000);
        return formatLineNumber(lineNumber);
    }

    public static String formatExchangeCode(int exchangeCode) {
        return String.format("%03d", exchangeCode);
    }

    public static String formatLineNumber(int lineNumber) {
        return String.format("%04d", lineNumber);
    }

    public static String format(int areaCode, int exchangeCode, int lineNumber) {
        validateAreaCode(areaCode);
        return String.format("%03d-%03d-%04d", areaCode, exchangeCode, lineNumber);
    }

    public static String format(String phoneStr) {
        validatePhoneStr(phoneStr);

        String areaCode = phoneStr.substring(0, 3);
        String exchangeCode = phoneStr.substring(3, 6);
        String lineNumber = phoneStr.substring(6, 10);
        // Format: AAA-NXX-XXXX
        return String.format("%03d-%03d-%04d", areaCode, exchangeCode, lineNumber);
    }

    public static boolean validateAreaCode(Integer areaCode) {
        if (areaCode == null || areaCode < 200 || areaCode > 999) {
            throw new IllegalArgumentException("Area code must be between 200 and 999.");
        }
        return true;
    }

    public static boolean validatePhoneStr(String phoneStr) {
        if (phoneStr == null || !phoneStr.matches("\\d{10}")) {
            LOGGER.debug(phoneStr);
            throw new IllegalArgumentException("Input must be exactly 10 digits.");
        }
        return true;
    }
}
