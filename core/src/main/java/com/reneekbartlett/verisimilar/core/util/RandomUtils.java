package com.reneekbartlett.verisimilar.core.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    private RandomUtils() {
        //
    }

    public static String getRandomAreaCode() {
        return RandomUtils.getRandomDigitString(3, 200, 999);
    }

    public static String getRandomDigitString(int digits, int min, int max) {
        if (digits <= 0) {
            throw new IllegalArgumentException("digits must be > 0");
        }
        // Digit-based bounds
        int digitMin = (int) Math.pow(10, digits - 1);   // e.g., 3 → 100
        int digitMax = (int) Math.pow(10, digits) - 1;   // e.g., 3 → 999

        // Clamp min/max to digit range
        int lower = Math.max(min, digitMin);
        int upper = Math.min(max, digitMax);

        // If the caller's max is too small, clamp to digitMin
        if (upper < lower) {
            // No valid overlap → fallback to digit range only
            lower = digitMin;
            upper = digitMax;
        }

        int randomInt = ThreadLocalRandom.current().nextInt(lower, upper + 1);
        return String.valueOf(randomInt);
    }
    
}
