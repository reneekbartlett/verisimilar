package com.reneekbartlett.verisimilar.core.generator;

import java.time.LocalDate;
import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;

import com.reneekbartlett.verisimilar.core.model.Generation;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class BirthdayGenerator extends AbstractLocalDateGenerator {
    public static final int DEFAULT_MIN_YEAR;
    public static final int DEFAULT_MAX_YEAR;

    static {
        DEFAULT_MIN_YEAR = Year.now().getValue()-100; // oldest
        DEFAULT_MAX_YEAR = Year.now().getValue()-18; // most recent
    }

    public BirthdayGenerator() {
        // No config files / selector
    }

    @Override
    protected LocalDate generateLocalDate(DatasetResolutionContext ctx, SelectionFilter criteria) {
        return generateBirthday(ctx, criteria);
    }

    /* Birthday gets generated here */
    public LocalDate generateBirthday(DatasetResolutionContext context, SelectionFilter filter) throws IllegalArgumentException {
        int minYear = (!filter.minYear().isPresent()) ? DEFAULT_MIN_YEAR : filter.minYear().get();
        int maxYear = (!filter.maxYear().isPresent()) ? DEFAULT_MAX_YEAR : filter.maxYear().get();

        // TODO:  Generation
        //int year1 = criteria.generation().getStartYear();
        //int year2 = criteria.generation().getEndYear();

        // TODO:  Make sure min < max
        //int minYear = Math.min(year1, year2);
        //int maxYear = Math.min(Math.max(year1, year2), DEFAULT_MAX_YEAR); // Don't allow under min age (18)

        int year = randomBetween(minYear, maxYear);

        // If you know their birth year, determine how many days are in the given year (365 or 366)
        // Then, Generate a random day of the year (1–366)
        int maxDay = LocalDate.of(year, 12, 31).getDayOfYear();
        int day = ThreadLocalRandom.current().nextInt(1, maxDay + 1);
        return LocalDate.ofYearDay(year, day);
    }

    private int randomBetween(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    // TODO:  Move these to a Util class
    public static boolean validateYear(int year) throws IllegalArgumentException {
        if (year < 1 || year > 9999) {
            throw new IllegalArgumentException("Year must be between 1 and 9999. Provided: " + year);
        }
        return true;
    }

    // TODO:  Move these to a Util class
    public static boolean validateMonth(int month) throws IllegalArgumentException {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12. Provided: " + month);
        }
        return true;
    }

    // TODO:  Move these to a Util class
    public static boolean validateGeneration(Generation generation) throws IllegalArgumentException {
        if (generation == null || generation.getDisplayName() == Generation.UNKNOWN.getDisplayName()) {
            throw new IllegalArgumentException("Unrecognized Generation value. Provided: " + generation);
        }
        return true;
    }
}
