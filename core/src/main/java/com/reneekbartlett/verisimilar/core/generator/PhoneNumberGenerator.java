package com.reneekbartlett.verisimilar.core.generator;

import java.util.concurrent.ThreadLocalRandom;

import com.reneekbartlett.verisimilar.core.datasets.key.AreaCodeDatasetKey;
import com.reneekbartlett.verisimilar.core.generator.api.AbstractValueGenerator;
import com.reneekbartlett.verisimilar.core.model.PhoneNumber;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.util.FieldValidationUtils;
import com.reneekbartlett.verisimilar.core.selector.engine.AreaCodeSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;

/***
 * Map<String, String[]> AREACODES_BY_STATE ResourceMapLoader.loadArrayMap("/phone_areacode_bystate_us.txt");
 * EXCHANGE_CODES_BY_AREACODE
 * PHONE_NUMBER_TYPE
 */
public class PhoneNumberGenerator extends AbstractValueGenerator<PhoneNumber> {

    // TODO: EXCHANGE_CODES_BY_AREACODE
    // TODO: PHONE_NUMBER_TYPE
    // TODO: AreaCodeGenerator?

    private final AreaCodeSelectionEngine areaCodeSelector;

    public PhoneNumberGenerator(AreaCodeSelectionEngine areaCodeSelector) {
        this.areaCodeSelector = areaCodeSelector;
    }

    public PhoneNumberGenerator(DatasetSelectionEngineRegistry selectors) {
        this(selectors.areaCode());
    }

    @Override
    protected PhoneNumber generateValue(DatasetResolutionContext ctx, SelectionFilter filter) {
        AreaCodeDatasetKey key = AreaCodeDatasetKey.fromContext(ctx);
        return generatePhoneNumber(key, filter);
    }

    private PhoneNumber generatePhoneNumber(AreaCodeDatasetKey key, SelectionFilter filter) {
        String areaCode = generateAreaCode(key, filter);
        String exchangeCode = generateExchangeCode(filter);
        String lineNumber = generateLineNumber(filter);
        return new PhoneNumber(areaCode, exchangeCode, lineNumber);
    }

    private String generateAreaCode(AreaCodeDatasetKey areaCodeKey, SelectionFilter filter) {
        return filter.areaCode().orElseGet(() -> areaCodeSelector.select(areaCodeKey, filter));
    }

    // TODO:  Configure option to just get random for backup cases?
    @SuppressWarnings("unused")
    private String getRandomAreaCode() {
        int areaCode = ThreadLocalRandom.current().nextInt(200, 999+1);
        return String.valueOf(areaCode);
    }

    private String generateExchangeCode(SelectionFilter filter) {
        // Generate exchange code (NXX)
        int firstDigit = ThreadLocalRandom.current().nextInt(2, 10); // 2–9
        int secondDigit = ThreadLocalRandom.current().nextInt(0, 10);
        int thirdDigit  = ThreadLocalRandom.current().nextInt(0, 10);

        int exchangeCode = firstDigit * 100 + secondDigit * 10 + thirdDigit;

        return formatExchangeCode(exchangeCode);
    }

    private String generateLineNumber(SelectionFilter filter) {
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
        FieldValidationUtils.validateAreaCode(areaCode);
        return String.format("%03d-%03d-%04d", areaCode, exchangeCode, lineNumber);
    }

    public static String format(String phoneStr) {
        FieldValidationUtils.validatePhoneStr(phoneStr);

        String areaCode = phoneStr.substring(0, 3);
        String exchangeCode = phoneStr.substring(3, 6);
        String lineNumber = phoneStr.substring(6, 10);
        // Format: AAA-NXX-XXXX
        return String.format("%03d-%03d-%04d", areaCode, exchangeCode, lineNumber);
    }

    @Override
    protected Class<PhoneNumber> valueType() {
        return PhoneNumber.class;
    }
}
