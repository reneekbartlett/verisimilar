package com.reneekbartlett.verisimilar.core.util;

import java.util.EnumSet;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    private RandomUtils() {
        //
    }

    public static <T> T getRandom(T[] array, ThreadLocalRandom rand) {
        return array[rand.nextInt(array.length)];
    }

    public static String getRandomAreaCode() {
        return RandomUtils.getRandomDigitString(3, 200, 999);
    }

    public static <T extends Enum<T>> T getRandomFromSet(EnumSet<T> set, java.util.function.ToDoubleFunction<T> weightExtractor) {
        if (set == null || set.isEmpty()) {
            throw new IllegalArgumentException("EnumSet cannot be null or empty.");
        }

        // 1. Calculate total weight of the elements currently in the EnumSet
        double totalWeight = 0.0;
        for (T item : set) {
            totalWeight += weightExtractor.applyAsDouble(item);
        }

        if (totalWeight <= 0.0) {
            throw new IllegalArgumentException("Total weight of the elements must be greater than 0.");
        }

        // 2. Generate a random target between 0.0 (inclusive) and totalWeight (exclusive)
        double target = ThreadLocalRandom.current().nextDouble() * totalWeight;

        // 3. Linearly scan the subset and subtract weights to find the matching bucket
        for (T item : set) {
            target -= weightExtractor.applyAsDouble(item);
            if (target <= 0.0) {
                return item;
            }
        }

        // Fallback edge case for rounding errors
        return set.iterator().next();
    }

    /***
     * 
     * @param digits
     * @param min
     * @param max
     * @return
     */
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

    /***
     * The power value 
     * @param min
     * @param max
     * @param skewPower Determines the skew. Value >1 skews towards the minimum (lower numbers), <1 skews towards the maximum (higher numbers)
     * @return
     */
    public static int getSkewedRandom(int min, int max, double skewPower) {
        Random random = new Random();
        // Generate a uniformly random double between 0.0 (inclusive) and 1.0 (exclusive)
        double uniformRandom = random.nextDouble();

        // Skew the value using Math.pow()
        double skewedRandom = Math.pow(uniformRandom, skewPower);

        // Map the skewed value to the desired range [min, max]
        // (max - min) + min ensures the range is inclusive of max
        int range = max - min + 1;
        int result = (int) (skewedRandom * range) + min;

        // Ensure the result is within the specified range due to casting and bounds
        return Math.min(Math.max(result, min), max);
    }
}
